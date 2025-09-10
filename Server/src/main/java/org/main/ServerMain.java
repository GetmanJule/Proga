package org.main;

import org.data.inner.Movie;
import org.inner.ConsoleIO;
import org.inner.commands.CommandManager;
import org.inner.commands.Commands;
import org.inner.commands.SaveCommand;
import org.inner.utils.XMLManager;
import org.outer.Server;

import java.io.IOException;
import java.util.ArrayList;

public class ServerMain {
    public static String filePath = "C:\\Users\\user\\Desktop\\lab5\\server\\src\\main\\resources\\save.xml";//Программа пытается считать путь из переменной окружения системы с именем data

    public static void main(String[] args) throws IOException {

        XMLManager xmlManager = new XMLManager();
        ArrayList<Movie> movies = new ArrayList<>();
        xmlManager.setData(movies);

        try {
            XMLManager.loadData(filePath);//вызываем фуекцию для использования файла
            movies = XMLManager.getData();
            movies.sort(Movie::compareTo);
            System.out.println("All movies is loaded!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading data");
        }

        Server server = new Server();
        System.out.println("Сервер ожидает подключение клиента!");
        new Thread(() -> {
            server.connect();
        }).start();

        ConsoleIO consoleIO = new ConsoleIO();
        while (true) {
            if (consoleIO.write().equals("save")) {
                new SaveCommand().doo();
                System.out.println("Коллекция сохранена");
            } else {
                System.out.println("Неизвестная команда");
            }
        }
    }
}