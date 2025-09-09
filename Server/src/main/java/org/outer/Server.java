package org.outer;

import org.data.AnswerDto;
import org.data.RequestDto;
import org.data.inner.Movie;
import org.inner.commands.Commands;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private final static int port = 8000;
    private final Commands cmd = new Commands();
    private final ArrayList<Movie> movies = new ArrayList<>();

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
                }

                String message = requestDto.getCommand();
                Movie movieToAdd = requestDto.getMovie();

                String responseStr;

                // Если клиент прислал Movie и команду add, пробуем добавить
                if ("add".equalsIgnoreCase(message) && movieToAdd != null) {
                    responseStr = cmd.commandsEditor(movies, message);
                } else if ("exit".equalsIgnoreCase(message)) {
                    responseStr = "Выход из программы";
                    System.out.println("Клиент отключен");
                    out.writeObject(new AnswerDto(null, responseStr));
                    out.flush();
                    break;
                } else {
                    // Иные команды
                    responseStr = cmd.commandsEditor(movies, message);
                }

                AnswerDto answerDto = new AnswerDto(null, responseStr);
                out.writeObject(answerDto);
                out.flush();
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
