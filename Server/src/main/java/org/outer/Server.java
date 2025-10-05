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
import java.net.SocketException;
import java.util.ArrayList;

public class Server {

    private final static int port = 45887;
    private final Commands cmd = new Commands();
    private static final ArrayList<Movie> movies = XMLManager.getData();

    public void connect() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);

            // основной цикл — сервер живет постоянно
            while (true) {
                System.out.println("Ожидание подключения клиента...");
                try (Socket clientSocket = serverSocket.accept();
                     DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                     DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {

                    System.out.println("Клиент подключен: " + clientSocket.getInetAddress());

                    while (true) {
                        // --- Чтение объекта ---
                        int length;
                        try {
                            length = dis.readInt(); // 4 байта длины
                        } catch (EOFException | SocketException e) {
                            System.out.println("Клиент завершил сессию");
                            break;
                        }

                        byte[] data = new byte[length];
                        try {
                            dis.readFully(data);
                        } catch (IOException e) {
                            System.out.println("Ошибка чтения данных от клиента: " + e.getMessage());
                            break;
                        }

                        RequestDto requestDto;
                        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                            Object obj = ois.readObject();
                            if (!(obj instanceof RequestDto)) {
                                System.out.println("Получен неверный объект: " + obj.getClass());
                                sendObject(dos, new AnswerDto(null, "Ошибка: получен неверный объект"));
                                continue;
                            }
                            requestDto = (RequestDto) obj;
                        } catch (Exception e) {
                            System.out.println("Ошибка десериализации запроса: " + e.getMessage());
                            sendObject(dos, new AnswerDto(null, "Ошибка: данные повреждены или неверный формат"));
                            continue;
                        }

                        // --- Логика обработки ---
                        String message = requestDto.getCommand();
                        Movie movieArg = requestDto.getMovie();
                        String responseStr = "Ошибка: команда пустая!";

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
                        } else if (message.toLowerCase().startsWith("remove_greater") && movieArg != null) {
                            responseStr = cmd.commandsEditor(movies, "remove_greater", movieArg);
                        } else if ("exit".equalsIgnoreCase(message)) {
                            responseStr = "Выход из программы";
                            System.out.println("Клиент отключен");
                            sendObject(dos, new AnswerDto(null, responseStr));
                            break;
                        } else {
                            responseStr = cmd.commandsEditor(movies, message, null);
                        }

                        // --- Отправка ответа ---
                        sendObject(dos, new AnswerDto(null, responseStr));
                    }

                } catch (IOException e) {
                    System.out.println("Ошибка при работе с клиентом: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            SaveCommand saveCommand = new SaveCommand();
            saveCommand.doo();
            System.out.println("Данные сохранены");
        }
    }

    private void sendObject(DataOutputStream dos, Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(obj);
            }
            byte[] data = baos.toByteArray();
            dos.writeInt(data.length);
            dos.write(data);
            dos.flush();
        } catch (IOException e) {
            System.out.println("Ошибка отправки данных клиенту: " + e.getMessage());
        }
    }
}
