package org.outer;

import org.data.AnswerDto;
import org.data.RequestDto;
import org.data.inner.Movie;
import org.inner.commands.CommandManager;
import org.inner.commands.Commands;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

            byte[] buffer = new byte[4096];
            int bytesRead;

            // цикл обработки сообщений
            while ((bytesRead = in.read(buffer)) != -1) {
                RequestDto requestDto = (RequestDto) fromBytes(buffer);
                String message = requestDto.getCommand();
                //todo:пофиксить nullPointer
                System.out.println("Получено: " + message);

                // формируем ответ
                String response = "Answer from server: " + checkString(message, clientSocket) + "\n";

                AnswerDto answerDto = new AnswerDto(null, response);
                out.write(toBytes(answerDto));
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
            try {
                String ans = cmd.commandsEditor(movies, line);
                return ans;
            } catch (NullPointerException e) {
                return "Некорректная команда, посмотрите в /help";

            }
        } else {
            return "Ошибка!";
        }
    }

    public static Object fromBytes(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public static byte[] toBytes(Object obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);  // сериализуем объект
            oos.flush();
            return bos.toByteArray();  // получаем массив байтов
        }
    }
}
