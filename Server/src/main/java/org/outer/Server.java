package org.outer;

import org.data.AnswerDto;
import org.data.RequestDto;
import org.data.inner.Movie;
import org.inner.commands.CommandManager;
import org.inner.commands.Commands;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

            // Создаём ObjectInputStream и ObjectOutputStream один раз
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // Цикл обработки сообщений
            while (true) {
                RequestDto requestDto;
                try {
                    requestDto = (RequestDto) in.readObject(); // читаем объект целиком
                } catch (ClassNotFoundException e) {
                    System.out.println("Получен неизвестный объект");
                    continue;
                }

                String message = requestDto.getCommand();
                System.out.println("Получено: " + message);

                // формируем ответ
                String responseStr = checkString(message, clientSocket);
                AnswerDto answerDto = new AnswerDto(null, responseStr);

                out.writeObject(answerDto); // отправляем объект клиенту
                out.flush();

                // команда exit закрывает соединение
                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Клиент отключен");
                    break;
                }
            }

            clientSocket.close();
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
                } catch (IOException e) {}
                return "Выход из программы";
            }
            try {
                return cmd.commandsEditor(movies, line);
            } catch (NullPointerException e) {
                return "Некорректная команда, посмотрите в /help";
            }
        } else {
            return "Ошибка!";
        }
    }
}
