package org.commands;

import org.Static.CommandManager;
import org.classes.Movie;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*

 */
public class ExecuteCommand implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        String[] idS = s.split(" ");
        String filename = idS[1];
        ExecuteCommand ex = new ExecuteCommand();
        CommandManager cmd = new CommandManager();
        Commands c = new Commands();

        try {
            Scanner sc = new Scanner(new File("./scripts/" + filename));//чтение данных
            CommandManager.fileQueue.add(filename);

            while(sc.hasNextLine()) {//пока есть что считывать
                String line = sc.nextLine();//следующая строка типа String
                if (line.split(" ").length >= 2 && line.split(" ")[0].equals("execute_script") && CommandManager.fileQueue.contains(line.split(" ")[1])) {
                    //если длина больше 2: 0 значение 1 слово сама команда, 1 значение 2 слово имя файла
                    System.out.println("Recursively executing!"); //если, в команде опять открывается файл, то ошибка, чтобы не было рекурсива
                }else if(line.split(" ").length >= 2 && line.split(" ")[0].equals("execute_script") && !CommandManager.fileQueue.contains(line.split(" ")[1])) {
                    //
                    ex.doo(mySet, line);
                }
                else if (!line.isEmpty()) {
                    try {
                        CommandManager.listOfCommand.get(line.split(" ")[0]).doo(mySet, line);//ищет команду в commandManager
                        System.out.println();
                    }
                    catch (Exception e) {
                        System.out.println("No such command!");
                        System.out.println();
                    }
                }
            }
            return true;
        } catch (FileNotFoundException e) {//нет файла со скриптом
            System.out.println("Wrong script file!");
            return false;
        }
    }

    @Override
    public String des() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
    }

    @Override
    public String getName() {
        return "execute_script";
    }
}
