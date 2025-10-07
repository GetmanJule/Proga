package org.outer;

import org.data.AnswerDto;
import org.data.RequestDto;
import org.data.inner.Movie;
import org.inner.MovieRepository;
import org.inner.commands.Commands;
import org.inner.commands.SaveCommand;
import org.inner.utils.XMLManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.*;

public class Server {

    private final static int port = 45887;
    private final Commands cmd = new Commands();

    private final ExecutorService readPool = Executors.newCachedThreadPool();
    private final ExecutorService processPool = Executors.newFixedThreadPool(4);
    private final ExecutorService sendPool = Executors.newFixedThreadPool(4);
    public final List<Movie> movies;
    public Server(){
        movies = MovieRepository.loadAll();
    }

    public void save() {
        new SaveCommand().doo(movies);
    }

    public void start() throws Exception {
        System.out.println("Movies loaded in memory: " + movies.size());

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                readPool.submit(() -> handleClient(clientSocket, movies));
            }
        } finally {
            // при завершении сервера сохраняем все изменения в БД
            System.out.println("Data saved on server shutdown");
        }
    }

    private void handleClient(Socket clientSocket, List<Movie> movies) {
        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {

            while (true) {
                int length;
                try {
                    length = dis.readInt();
                } catch (IOException e) {
                    System.out.println("Client disconnected");
                    break;
                }

                byte[] data = new byte[length];
                dis.readFully(data);

                processPool.submit(() -> {
                    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                        Object obj = ois.readObject();
                        if (!(obj instanceof RequestDto)) return;
                        RequestDto request = (RequestDto) obj;

                        String login = request.getLogin();
                        String password = request.getPassword();
                        String command = request.getCommand();
                        Movie movieArg = request.getMovie();
                        String answer;

                        // --- регистрация и логин ---
                        if ("register".equalsIgnoreCase(command)) {
                            try {
                                boolean registered = DatabaseManager.registerUser(login, password);
                                answer = registered ? "Registration success" : "Registration failed (user exists)";
                            } catch (SQLException | NoSuchAlgorithmException e) {
                                answer = "Error: " + e.getMessage();
                            }
                        } else if ("login".equalsIgnoreCase(command)) {
                            try {
                                boolean auth = DatabaseManager.authenticateUser(login, password);
                                answer = auth ? "Login success" : "Login failed";
                            } catch (SQLException | NoSuchAlgorithmException e) {
                                answer = "Error: " + e.getMessage();
                            }
                        } else {
                            // --- проверка авторизации ---
                            try {
                                if (!DatabaseManager.authenticateUser(login, password)) {
                                    answer = "Unauthorized. Please login first.";
                                } else {
                                    // --- обработка команд только с коллекцией в памяти ---
                                    answer = handleCommand(command, movieArg, login, movies);
                                }
                            } catch (Exception e) {
                                answer = "Error: " + e.getMessage();
                            }
                        }

                        String finalAnswer = answer;
                        sendPool.submit(() -> sendResponse(dos, finalAnswer));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String handleCommand(String command, Movie movieArg, String login, List<Movie> movies) {
        String response = "Unknown command";
        if (command == null || command.isEmpty()) {
            return "Ошибка: команда пустая!";
        }

        command = command.toLowerCase();

        switch (command.split(" ")[0]) {
            case "add":
                if (movieArg != null) {
                    response = cmd.commandsEditor(movies, "add", movieArg, login);
                } else response = "Ошибка: объект фильма не передан!";
                break;
            case "update":
                if (movieArg != null) {
                    String[] parts = command.split(" ");
                    if (parts.length != 2) response = "Ошибка: update <id>";
                    else response = cmd.commandsEditor(movies, "update " + parts[1], movieArg, login);
                } else response = "Ошибка: объект фильма не передан!";
                break;
            case "remove_greater":
                if (movieArg != null) {
                    response = cmd.commandsEditor(movies, "remove_greater", movieArg, login);
                } else response = "Ошибка: объект фильма не передан!";
                break;
            case "clear":
                response = cmd.commandsEditor(movies, "clear", null, login);
                break;
            case "exit":
                response = "Выход из программы";
                break;
            default:
                response = cmd.commandsEditor(movies, command, null, login);
        }

        return response;
    }

    private void sendResponse(DataOutputStream dos, String answer) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(new AnswerDto(null, answer));
            byte[] bytes = baos.toByteArray();
            dos.writeInt(bytes.length);
            dos.write(bytes);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
