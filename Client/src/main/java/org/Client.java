package org;

import org.data.AnswerDto;
import org.data.RequestDto;
import org.data.inner.Movie;
import org.inner.ConsoleIO;
import org.inner.commands.ClientCommandManager;
import org.inner.commands.UpdateCommand;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class Client {

    private static final String HOST = "localhost";
    private static final int PORT = 45887;
    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_MS = 2000;
    private static final int CONNECT_TIMEOUT_MS = 3000;
    private static final int SELECT_TIMEOUT_MS = 500;

    private final ClientCommandManager commandManager;

    public Client(ClientCommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void connect(ConsoleIO consoleIO) {
        SocketChannel channel = tryConnectNonBlocking();
        if (channel == null) {
            System.out.println("Не удалось подключиться после " + MAX_RETRIES + " попыток.");
            return;
        }

        System.out.println("Подключено к серверу");

        try (Selector selector = Selector.open()) {
            channel.register(selector, SelectionKey.OP_READ);

            while (true) {
                String msg = consoleIO.write();
                if (msg == null || msg.isEmpty()) continue;

                // формируем запрос
                RequestDto requestDto = new RequestDto();
                Movie movie = null;

                if (msg.toLowerCase().startsWith("update")) {
                    UpdateCommand updateCommand = new UpdateCommand();
                    if (!updateCommand.parseCommand(msg)) {
                        System.out.println("Команда update введена неверно. Используйте: update {id}");
                        continue;
                    }
                    movie = updateCommand.doo();
                    requestDto.setCommand("update " + updateCommand.getId());
                    requestDto.setMovie(movie);
                } else {
                    movie = commandManager.execute(msg);
                    if (movie != null) requestDto.setMovie(movie);
                    requestDto.setCommand(msg);
                }

                // отправляем запрос
                ByteBuffer outBuffer = serializeWithLength(requestDto);
                writeFully(channel, outBuffer, selector);

                // ждём ответ
                Object obj = readObjectNonBlocking(channel, selector);
                if (obj == null) {
                    System.out.println("Сервер закрыл соединение или истёк таймаут.");
                    break;
                }

                if (!(obj instanceof AnswerDto answerDto)) {
                    System.out.println("Сервер прислал неожиданный объект: " + obj.getClass());
                    continue;
                }

                System.out.println("Ответ сервера: " + answerDto.getAnswer());

                if ("exit".equalsIgnoreCase(msg.trim())) {
                    System.out.println("Завершение работы клиента...");
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка клиента: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Попытка подключения с Selector в неблокирующем режиме
     */
    private SocketChannel tryConnectNonBlocking() {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                SocketChannel channel = SocketChannel.open();
                channel.configureBlocking(false);
                channel.connect(new InetSocketAddress(HOST, PORT));

                Selector selector = Selector.open();
                channel.register(selector, SelectionKey.OP_CONNECT);

                long startTime = System.currentTimeMillis();
                boolean connected = false;

                while (System.currentTimeMillis() - startTime < CONNECT_TIMEOUT_MS) {
                    if (selector.select(200) > 0) {
                        Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                        while (iter.hasNext()) {
                            SelectionKey key = iter.next();
                            iter.remove();

                            if (key.isConnectable()) {
                                SocketChannel sc = (SocketChannel) key.channel();
                                if (sc.finishConnect()) {
                                    connected = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (connected) break;
                }

                selector.close();

                if (connected) return channel;

                System.out.println("Попытка " + attempt + " не удалась (таймаут подключения).");
                channel.close();
                Thread.sleep(RETRY_DELAY_MS);

            } catch (Exception e) {
                System.out.println("Попытка " + attempt + " не удалась: " + e.getMessage());
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ignored) {}
            }
        }
        return null;
    }

    /**
     * Неблокирующая запись всего буфера
     */
    private void writeFully(SocketChannel channel, ByteBuffer buffer, Selector selector) throws IOException {
        channel.register(selector, SelectionKey.OP_WRITE);
        while (buffer.hasRemaining()) {
            selector.select(SELECT_TIMEOUT_MS);
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if (key.isWritable()) {
                    channel.write(buffer);
                }
            }
        }
        channel.register(selector, SelectionKey.OP_READ); // возвращаем в режим чтения
    }

    /**
     * Неблокирующее чтение объекта с length-prefixed протоколом
     */
    private Object readObjectNonBlocking(SocketChannel channel, Selector selector)
            throws IOException, ClassNotFoundException {

        // читаем длину (4 байта)
        ByteBuffer lenBuf = ByteBuffer.allocate(4);
        if (!readFullyNonBlocking(channel, selector, lenBuf))
            return null;
        lenBuf.flip();
        int length = lenBuf.getInt();

        // читаем полезные данные
        ByteBuffer dataBuf = ByteBuffer.allocate(length);
        if (!readFullyNonBlocking(channel, selector, dataBuf))
            return null;

        dataBuf.flip();
        byte[] objectData = new byte[length];
        dataBuf.get(objectData);

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(objectData))) {
            return ois.readObject();
        }
    }

    /**
     * Читает буфер полностью с таймаутом
     */
    private boolean readFullyNonBlocking(SocketChannel channel, Selector selector, ByteBuffer buffer) throws IOException {
        long start = System.currentTimeMillis();
        while (buffer.hasRemaining() && (System.currentTimeMillis() - start) < CONNECT_TIMEOUT_MS) {
            selector.select(SELECT_TIMEOUT_MS);
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if (key.isReadable()) {
                    int bytesRead = channel.read(buffer);
                    if (bytesRead == -1) return false; // сервер закрыл соединение
                }
            }
        }
        return !buffer.hasRemaining();
    }

    /**
     * Сериализация объекта в ByteBuffer (length + data)
     */
    private ByteBuffer serializeWithLength(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
        }
        byte[] data = baos.toByteArray();
        ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
        buffer.putInt(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
