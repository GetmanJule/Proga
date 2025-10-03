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
import java.nio.channels.SocketChannel;

public class Client {

    private final static String host = "localhost";
    private final static int port = 45887;
    private final static int MAX_RETRIES = 5;       // сколько раз пробовать подключиться
    private final static int RETRY_DELAY_MS = 2000; // задержка между попытками (2 сек)
    private final ClientCommandManager commandManager;

    public Client(ClientCommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void connect(ConsoleIO consoleIO) {
        try (SocketChannel channel = tryConnect()) {
            if (channel == null) {
                System.out.println("Не удалось подключиться к серверу после " + MAX_RETRIES + " попыток.");
                return;
            }

            System.out.println("Подключено к серверу");

            while (true) {
                String msg = consoleIO.write();
                if (msg == null || msg.isEmpty()) continue;

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

                // --- Отправка ---
                ByteBuffer writeBuffer = serializeWithLength(requestDto);
                while (writeBuffer.hasRemaining()) {
                    channel.write(writeBuffer);
                }

                // --- Чтение ---
                Object obj = readObject(channel);
                if (obj == null) {
                    System.out.println("Соединение с сервером потеряно.");
                    break;
                }

                if (!(obj instanceof AnswerDto)) {
                    System.out.println("Ошибка: сервер прислал неожиданный объект " + obj.getClass());
                    continue;
                }

                AnswerDto answerDto = (AnswerDto) obj;
                System.out.println("Ответ сервера: " + answerDto.getAnswer());

                if ("exit".equalsIgnoreCase(msg.trim())) {
                    System.out.println("Завершение работы клиента...");
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Ошибка клиента: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Попытка подключения к серверу с несколькими ретраями
     */
    private SocketChannel tryConnect() {
        for (int i = 1; i <= MAX_RETRIES; i++) {
            try {
                SocketChannel channel = SocketChannel.open();
                channel.configureBlocking(true); // блокирующий режим
                channel.socket().connect(new InetSocketAddress(host, port), 2000); // таймаут 2 сек
                return channel;
            } catch (IOException e) {
                System.out.println("Попытка " + i + " не удалась: " + e.getMessage());
                if (i < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ignored) {}
                }
            }
        }
        return null;
    }

    // Сериализация с длиной
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

    // Десериализация с учётом длины
    private Object readObject(SocketChannel channel) throws IOException, ClassNotFoundException {
        // читаем длину (4 байта)
        ByteBuffer lenBuf = ByteBuffer.allocate(4);
        while (lenBuf.hasRemaining()) {
            int bytesRead = channel.read(lenBuf);
            if (bytesRead == -1) return null; // сервер закрыл соединение
        }
        lenBuf.flip();
        int length = lenBuf.getInt();

        // читаем сам объект
        ByteBuffer dataBuf = ByteBuffer.allocate(length);
        while (dataBuf.hasRemaining()) {
            int bytesRead = channel.read(dataBuf);
            if (bytesRead == -1) return null;
        }

        dataBuf.flip();
        byte[] objectData = new byte[length];
        dataBuf.get(objectData);

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(objectData))) {
            return ois.readObject();
        }
    }
}
