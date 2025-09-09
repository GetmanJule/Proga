package org.main;

import org.data.inner.Movie;
import org.inner.commands.Commands;
import org.inner.utils.XMLManager;
import org.outer.Server;

import java.util.ArrayList;

public class ServerMain {
    public static String filePath = System.getenv("data");//Программа пытается считать путь из переменной окружения системы с именем data

    public static void main(String[] args){
        XMLManager xmlManager = new XMLManager();
        ArrayList<Movie> movies = new ArrayList<>();
        xmlManager.setData(movies);

        try {
            XMLManager.loadData(filePath);//вызываем фуекцию для использования файла
            movies = XMLManager.getData();
            System.out.println("All movies is loaded!");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error loading data");
        }

        Server server = new Server();
        System.out.println("Сервер ожидает подключение клиента!");
        server.connect();
    }
}