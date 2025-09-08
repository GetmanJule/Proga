package org;

import org.data.AnswerDto;
import org.data.RequestDto;
import org.inner.ConsoleIO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {

    private final static String host = "localhost";
    private final static int port = 8000;

    public void connect(ConsoleIO consoleIO) {
        SocketChannel client = null;
        while (true) {
            try {
                // создаём канал
                client = SocketChannel.open();
                client.configureBlocking(true); // лучше блокирующий режим для простоты

                // подключаемся
                client.connect(new InetSocketAddress(host, port));
                System.out.println("Подключено к серверу");

                // буфер
                ByteBuffer buffer = ByteBuffer.allocate(4096);


                // цикл отправки/чтения команд
                while (true) {
                    String msg = consoleIO.write(); // читаем ввод пользователя
                    if (msg == null || msg.isEmpty()) continue;

                    // отправляем
                    buffer.clear();
                    buffer.put(toBytes(new RequestDto(null, msg)));
                    buffer.flip();
                    client.write(buffer);

                    // если команда exit → сразу выходим
                    if ("exit".equalsIgnoreCase(msg.trim())) {
                        System.out.println("Завершение работы клиента...");
                        return;
                    }

                    // читаем ответ
                    buffer.clear();
                    int bytesRead = client.read(buffer);
                    if (bytesRead == -1) {
                        System.out.println("Сервер закрыл соединение");
                        break;
                    }
                    AnswerDto answerDto = (AnswerDto) fromBytes(buffer.array());
                    System.out.println("Ответ сервера: " + answerDto.getAnswer());
                }

                client.close();

            } catch (IOException e) {
                System.out.println("Сервер недоступен, повтор через 1 секунду...");
                try {
                    if (client != null && client.isOpen()) {
                        client.close();
                    }
                } catch (IOException ignored) {
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public static byte[] toBytes(Object obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);  // сериализуем объект
            oos.flush();
            return bos.toByteArray();  // получаем массив байтов
        }
    }

    public static Object fromBytes(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
