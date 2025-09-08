package org.main;

import org.inner.commands.CommandManager;
import org.data.Movie;
import org.inner.commands.Commands;
import org.inner.utils.XMLManager;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.ArrayList;

public class Main {
    public static String filePath = System.getenv("data");//Программа пытается считать путь из переменной окружения системы с именем data
    public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        XMLManager xmlManager = new XMLManager();
        ArrayList<Movie> movies = new ArrayList<>();
        xmlManager.setData(movies);
        Commands cmd = new Commands();

        try {
            XMLManager.loadData(filePath);//вызываем фуекцию для использования файла
            movies = XMLManager.getData();
            System.out.println("All movies is loaded!");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error loading data");
        }

        System.out.print(">>> ");
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String line;
                while (!(line = reader.readLine()).equals("exit")) {
                    /*if (line.equals("save")){
                        XMLManager.writeToFile(filePath);
                        System.out.println("Datas saved!");
                    }*/

                    if(line.equals("exit")){
                        break;
                    }

                    try {
                        if(line.split(" ")[0].equals("execute_script")){
                            CommandManager.fileQueue.clear();
                        }
                        if(line.isEmpty()){
                            //System.out.print(">>> ");
                        }else {
                            cmd.commandsEditor(movies, line);
                        }
                    }catch (Exception e){
                        //System.out.println(e);
                        System.out.println("No such command!");
                    }


                    System.out.print(">>> ");
                }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}