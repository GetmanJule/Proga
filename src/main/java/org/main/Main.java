package org.main;

import org.Static.CommandManager;
import org.classes.Coordinates;
import org.classes.Location;
import org.classes.Movie;
import org.classes.Person;
import org.commands.Commands;
import org.enums.Color;
import org.enums.Country;
import org.enums.MpaaRating;
import org.managerData.XMLManager;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

/*
Main Class - eto main class
 */

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