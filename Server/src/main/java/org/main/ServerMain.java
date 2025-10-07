package org.main;

import org.outer.Server;
import org.inner.ConsoleIO;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        System.out.println("Сервер ожидает подключение клиента!");

        // Запуск сервера в отдельном потоке
        new Thread(() -> {
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Консоль для администрирования сервера
        ConsoleIO consoleIO = new ConsoleIO();
        while (true) {
            String input = consoleIO.write();
            if (input == null) continue;

            switch (input.trim().toLowerCase()) {
                case "save":
                    server.save();
                    break;
                case "exit":
                    System.out.println("Вы уверены что данные сохранены? 1/0 (yes/no)");
                    input = consoleIO.write();
                    if (input.strip().equals("1") || input.strip().equals("yes")) {
                        System.out.println("Завершение работы сервера...");
                        System.exit(0);
                    }
                    if (input.strip().equals("0") || input.strip().equals("no")) {
                        System.out.println("Введите команду save!");
                    }else {
                        System.out.println("Непонятная команда!");
                    }
                    break;
                default:
                    System.out.println("Неизвестная команда. Используйте 'exit' для завершения сервера.");
            }
        }
    }
}
