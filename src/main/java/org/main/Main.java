package org.main;

import org.outer.Server;

public class Main {
    public static String filePath = System.getenv("data");//Программа пытается считать путь из переменной окружения системы с именем data

    public static void main(String[] args) {
        Server server = new Server();
        server.connect();
    }
}