package org.outer;

import org.data.AnswerDto;
import org.data.RequestDto;
import org.data.inner.Movie;
import org.inner.commands.Commands;
import org.inner.commands.SaveCommand;
import org.inner.utils.XMLManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Server {

    private final static int port = 45887;

    private final Commands cmd = new Commands();
    private static final ArrayList<Movie> xmlMovies = XMLManager.getData();

    private final ExecutorService readPool = Executors.newCachedThreadPool();
    private final ExecutorService processPool = Executors.newFixedThreadPool(4);
    private final ExecutorService sendPool = Executors.newFixedThreadPool(4);

    public void start() throws Exception {
        DatabaseManager.loadAllMovies();
        System.out.println("Movies loaded in memory: " + DatabaseManager.getMovieList().size());

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                readPool.submit(() -> handleClient(clientSocket));
            }
        } finally {
            new SaveCommand().doo();
            System.out.println("Data saved on server shutdown");
        }
    }

    private void handleClient(Socket clientSocket) {
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
                            // --- проверка авторизации для всех остальных команд ---
                            try {
                                if (!DatabaseManager.authenticateUser(login, password)) {
                                    answer = "Unauthorized. Please login first.";
                                } else {
                                    // --- обработка команд через старую логику ---
                                    answer = handleCommand(command, movieArg);
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

    /**
     * Обработка команд add/update/remove_greater/exit и всех остальных через старую Commands
     */
    private String handleCommand(String command, Movie movieArg) {
        String response = "Unknown command";

        if (command == null || command.isEmpty()) {
            response = "Ошибка: команда пустая!";
        } else if ("add".equalsIgnoreCase(command) && movieArg != null) {
            response = cmd.commandsEditor(DatabaseManager.getMovieList(), "add", movieArg);
        } else if (command.toLowerCase().startsWith("update") && movieArg != null) {
            String[] parts = command.split(" ");
            if (parts.length != 2) {
                response = "Ошибка: команда update должна иметь вид 'update <id>'";
            } else {
                response = cmd.commandsEditor(DatabaseManager.getMovieList(), "update " + parts[1], movieArg);
            }
        } else if (command.toLowerCase().startsWith("remove_greater") && movieArg != null) {
            response = cmd.commandsEditor(DatabaseManager.getMovieList(), "remove_greater", movieArg);
        } else if ("exit".equalsIgnoreCase(command)) {
            response = "Выход из программы";
        } else {
            response = cmd.commandsEditor(DatabaseManager.getMovieList(), command, null);
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
