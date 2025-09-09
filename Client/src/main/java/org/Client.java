package org;

import org.data.AnswerDto;
import org.data.RequestDto;
import org.data.inner.Movie;
import org.inner.ConsoleIO;
import org.inner.commands.ClientCommandManager;
import org.inner.commands.UpdateCommand;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

public class Client {

    private final static String host = "localhost";
    private final static int port = 8000;
    private final ClientCommandManager commandManager;

    public Client(ClientCommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void connect(ConsoleIO consoleIO) {
        SocketChannel clientChannel = null;
        int attempts = 0;
        int maxAttempts = 4; // максимум попыток подключения

        while ((clientChannel == null || !clientChannel.isOpen()) && attempts < maxAttempts) {
            attempts++;
            try {
                clientChannel = SocketChannel.open(new InetSocketAddress(host, port));
                System.out.println("Подключено к серверу");
            } catch (IOException e) {
                System.out.println("Сервер недоступен, попытка " + attempts + " из " + maxAttempts);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }

        if (clientChannel == null || !clientChannel.isOpen()) {
            System.out.println("Не удалось подключиться к серверу после " + maxAttempts + " попыток. Завершение работы клиента.");
            return; // выходим из метода
        }

        try (SocketChannel channel = clientChannel;
             ObjectOutputStream out = new ObjectOutputStream(Channels.newOutputStream(channel));
             ObjectInputStream in = new ObjectInputStream(Channels.newInputStream(channel))) {

            while (true) {
                String msg = consoleIO.write();
                if (msg == null || msg.isEmpty()) continue;

                RequestDto requestDto = new RequestDto();
                Movie movie = null;

                if (msg.toLowerCase().startsWith("update")) {
                    UpdateCommand updateCommand = new UpdateCommand();
                    if (!updateCommand.parseCommand(msg)) {
                        System.out.println("Команда update введена неверно. Используйте: update {id}");
                        continue;
                    }

                    movie = updateCommand.doo();
                    requestDto.setCommand("update " + updateCommand.getId());
                    requestDto.setMovie(movie);

                } else {
                    movie = commandManager.execute(msg);
                    if (movie != null) requestDto.setMovie(movie);
                    requestDto.setCommand(msg);
                }

                out.writeObject(requestDto);
                out.flush();

                AnswerDto answerDto = (AnswerDto) in.readObject();
                System.out.println("Ответ сервера: " + answerDto.getAnswer());

                if ("exit".equalsIgnoreCase(msg.trim())) {
                    System.out.println("Завершение работы клиента...");
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Сервер закрыл соединение");
        }
    }

}
