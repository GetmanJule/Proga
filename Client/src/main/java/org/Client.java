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
                client.configureBlocking(true); // лучше блокирующий режим для простоты

                // подключаемся
                client.connect(new InetSocketAddress(host, port));
                System.out.println("Подключено к серверу");

                // буфер
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                // цикл отправки/чтения команд
                while (true) {
                    String msg = consoleIO.write(); // читаем ввод пользователя
                    if (msg == null || msg.isEmpty()) continue;

                    // отправляем
                    buffer.clear();
                    buffer.put(msg.getBytes());
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
                    String response = new String(buffer.array(), 0, bytesRead);
                    System.out.println("Ответ сервера: " + response);
                }

                client.close();

            } catch (IOException e) {
                System.out.println("Сервер недоступен, повтор через 1 секунду...");
                try {
                    if (client != null && client.isOpen()) {
                        client.close();
                    }
                } catch (IOException ignored) {}
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
