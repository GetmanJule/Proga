package org;

import org.inner.ConsoleIO;

import java.io.IOException;
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
                client.configureBlocking(false);

                // пытаемся подключиться
                client.connect(new InetSocketAddress(host, port));

                // ждём установления соединения
                while (!client.finishConnect()) {
                    System.out.println("Ожидание подключения...");
                    Thread.sleep(200);
                }

                System.out.println("Подключено к серверу");

                // отправляем сообщение
                String msg = consoleIO.write();
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                client.write(buffer);

                // читаем ответ
                buffer.clear();
                int bytesRead;
                while ((bytesRead = client.read(buffer)) <= 0) {
                    System.out.println("Ожидание ответа...");
                    Thread.sleep(100);
                }

                String response = new String(buffer.array(), 0, bytesRead);
                System.out.println("Ответ сервера: " + response);

                client.close();
                break; // завершаем работу после ответа

            } catch (IOException | InterruptedException e) {
                System.out.println("Сервер недоступен, повтор через 1 секунду...");
                try {
                    if (client != null && client.isOpen()) {
                        client.close();
                    }
                } catch (IOException ignored) {
                }

                try {
                    Thread.sleep(1000); // подождать и попробовать снова
                } catch (InterruptedException ignored) {
                }
            }
        }


    }
}
