package org.main;

import org.outer.Server;
import org.inner.ConsoleIO;

public class ServerMain {

    public static void main(String[] args) throws Exception{
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
                case "exit":
                    System.out.println("Завершение работы сервера...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неизвестная команда. Используйте 'exit' для завершения сервера.");
            }
        }
    }
}
