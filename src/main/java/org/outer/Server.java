package org.outer;

import org.data.Movie;
import org.inner.commands.CommandManager;
import org.inner.commands.Commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

    private final static int port = 8000;
    Commands cmd = new Commands();
    ArrayList<Movie> movies = new ArrayList<>();

    public void connect() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);

            Socket clientSocket = serverSocket.accept();
            System.out.println("Клиент подключен: " + clientSocket.getInetAddress());

            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            // цикл обработки сообщений
            while ((bytesRead = in.read(buffer)) != -1) {
                String message = new String(buffer, 0, bytesRead).trim();
                System.out.println("Получено: " + message);

                // формируем ответ
                String response = "Answer from server: " + checkString(message, clientSocket) + "\n";
                out.write(response.getBytes());
                out.flush();

                // команда exit закрывает соединение
                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Клиент отключен");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String checkString(String clientRequest, Socket clientSocket) {
        String line = clientRequest;
        if (!line.isEmpty()) {
            if (line.split(" ")[0].equals("execute_script")) {
                CommandManager.fileQueue.clear();
                return "Execute script is success";
            }
            if (line.equals("exit")) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                }
                return "Выход из программы";
            }
            return cmd.commandsEditor(movies, line);
        } else {
            return "Ошибка!";
        }
    }
}
