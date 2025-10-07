package org.outer;

import org.data.AnswerDto;
import org.data.RequestDto;
import org.data.inner.Movie;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;

public class Server {

    private final static int port = 45887;

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

                        String answer = "Unauthorized";
                        Movie movieArg = request.getMovie();

                        try {
                            if (DatabaseManager.authenticateUser(login, password)) {
                                List<Movie> movies = DatabaseManager.getMovieList();

                                switch (request.getCommand().toLowerCase()) {
                                    case "add":
                                        if (movieArg != null) {
                                            DatabaseManager.addMovie(movieArg, login);
                                            answer = "Movie added";
                                        }
                                        break;
                                    case "show":
                                        answer = "Movies: " + movies.size();
                                        break;
                                    default:
                                        answer = "Unknown command";
                                }
                            }
                        } catch (Exception e) {
                            answer = "Error: " + e.getMessage();
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
