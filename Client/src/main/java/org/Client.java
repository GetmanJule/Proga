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

        // Ждём, пока сервер станет доступен
        while (clientChannel == null || !clientChannel.isOpen()) {
            try {
                clientChannel = SocketChannel.open(new InetSocketAddress(host, port));
                System.out.println("Подключено к серверу");
            } catch (IOException e) {
                System.out.println("Сервер недоступен, повтор через 1 секунду...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }

        try (SocketChannel channel = clientChannel;
             ObjectOutputStream out = new ObjectOutputStream(Channels.newOutputStream(channel));
             ObjectInputStream in = new ObjectInputStream(Channels.newInputStream(channel))) {

            while (true) {
                String msg = consoleIO.write();
                if (msg == null || msg.isEmpty()) continue;

                RequestDto requestDto = new RequestDto();
                Movie movie = null;

                // Проверка на команду update
                if (msg.toLowerCase().startsWith("update")) {
                    UpdateCommand updateCommand = new UpdateCommand();
                    if (!updateCommand.parseCommand(msg)) {
                        // Некорректный ввод update, просим пользователя заново
                        System.out.println("Команда update введена неверно. Используйте: update {id}");
                        continue;
                    }

                    // Если id корректный, создаем объект Movie для обновления
                    movie = updateCommand.doo();

                    // Добавляем id в команду, чтобы сервер знал, какой объект обновлять
                    requestDto.setCommand("update " + updateCommand.getId());
                    requestDto.setMovie(movie);

                } else {
                    // Для остальных команд вызываем общий менеджер
                    movie = commandManager.execute(msg);
                    if (movie != null) requestDto.setMovie(movie);
                    requestDto.setCommand(msg);
                }

                // Отправляем на сервер
                out.writeObject(requestDto);
                out.flush();

                // Читаем ответ
                AnswerDto answerDto = (AnswerDto) in.readObject();
                System.out.println("Ответ сервера: " + answerDto.getAnswer());

                if ("exit".equalsIgnoreCase(msg.trim())) {
                    System.out.println("Завершение работы клиента...");
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
