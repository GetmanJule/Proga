package org.outer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {

    private final static String host = "localhost";
    private final static int port = 8000;

    public static void main(String[] args) {

        try {
            // создаём канал и переводим в неблокирующий режим
            SocketChannel client = SocketChannel.open();
            client.configureBlocking(false);

            // инициируем соединение
            client.connect(new InetSocketAddress(host, port));

            System.out.println("Подключено к серверу");

            // отправляем сообщение
            String msg = "Привет, сервер!";
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}
