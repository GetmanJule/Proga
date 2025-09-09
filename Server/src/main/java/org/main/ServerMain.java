package org.main;

import org.inner.utils.XMLManager;
import org.outer.Server;

public class ServerMain {
    public static String filePath = System.getenv("data");//Программа пытается считать путь из переменной окружения системы с именем data

    public static void main(String[] args) throws Exception{
        Server server = new Server();
        server.connect();
    }
}