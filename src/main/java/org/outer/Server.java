package org.outer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


public class Server {

    private final static int port = 8000;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);

            // Ждем подключения клиента
            Socket clientSocket = serverSocket.accept();
            System.out.println("Клиент подключен: " + clientSocket.getInetAddress());

            // Потоки для обмена данными
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer); // читаем от клиента
            String message = new String(buffer, 0, bytesRead);
            System.out.println("Получено: " + message);

            // Отправляем ответ клиенту
            String response = "Echo: " + message;
            out.write(response.getBytes());

            // Закрываем соединение
            System.out.println("Клиент отключен");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
