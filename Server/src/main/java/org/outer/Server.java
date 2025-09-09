package org.outer;

import org.data.AnswerDto;
import org.data.RequestDto;
import org.data.inner.Movie;
import org.inner.commands.Commands;
import org.inner.commands.SaveCommand;
import org.inner.utils.XMLManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private final static int port = 8000;
    private final Commands cmd = new Commands();
    private static final ArrayList<Movie> movies = XMLManager.getData();


    public void connect() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);

            Socket clientSocket = serverSocket.accept();
            System.out.println("Клиент подключен: " + clientSocket.getInetAddress());

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            while (true) {
                RequestDto requestDto;
                try {
                    requestDto = (RequestDto) in.readObject();
                } catch (ClassNotFoundException e) {
                    System.out.println("Получен неизвестный объект");
                    continue;
                } catch (EOFException e) {
                    System.out.println("Клиент завершил сессию");
                    break;
                }

                String message = requestDto.getCommand();
                Movie movieArg = requestDto.getMovie();

                String responseStr;

                if (message == null || message.isEmpty()) {
                    responseStr = "Ошибка: команда пустая!";
                } else if ("add".equalsIgnoreCase(message) && movieArg != null) {
                    responseStr = cmd.commandsEditor(movies, "add", movieArg);
                } else if (message.toLowerCase().startsWith("update") && movieArg != null) {
                    String[] parts = message.split(" ");
                    if (parts.length != 2) {
                        responseStr = "Ошибка: команда update должна иметь вид 'update <id>'";
                    } else {
                        responseStr = cmd.commandsEditor(movies, "update " + parts[1], movieArg);
                    }
                } else if ("exit".equalsIgnoreCase(message)) {
                    responseStr = "Выход из программы";
                    System.out.println("Клиент отключен");
                    out.writeObject(new AnswerDto(null, responseStr));
                    out.flush();
                    break;
                } else {
                    responseStr = cmd.commandsEditor(movies, message, null);
                }

                AnswerDto answerDto = new AnswerDto(null, responseStr);
                out.writeObject(answerDto);
                out.flush();
            }

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Всегда сохраняем при завершении сервера
            new SaveCommand().doo();
            System.out.println("Данные сохранены");
        }

    }
}
