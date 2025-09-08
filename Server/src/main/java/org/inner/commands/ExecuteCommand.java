package org.inner.commands;

import org.data.inner.Movie;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*

 */
public class ExecuteCommand implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        String[] idS = s.split(" ");
        String filename = idS[1];
        ExecuteCommand ex = new ExecuteCommand();

        try (Scanner sc = new Scanner(new File("./scripts/" + filename))) {
            CommandManager.fileQueue.add(filename);

            StringBuilder result = new StringBuilder("Выполнение скрипта '" + filename + "':\n");

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");
                String commandName = parts[0];

                if (parts.length >= 2 && commandName.equals("execute_script")) {
                    String nestedFile = parts[1];
                    if (CommandManager.fileQueue.contains(nestedFile)) {
                        result.append("Обнаружена рекурсия при попытке выполнить '")
                                .append(nestedFile).append("'\n");
                    } else {
                        result.append(ex.doo(mySet, line)).append("\n");
                    }
                } else {
                    try {
                        String output = CommandManager.listOfCommand.get(commandName).doo(mySet, line);
                        result.append(output).append("\n");
                    } catch (Exception e) {
                        result.append("Неизвестная команда: ").append(commandName).append("\n");
                    }
                }
            }
            return result.append("Скрипт '").append(filename).append("' завершён.\n").toString();

        } catch (FileNotFoundException e) {
            return "Файл '" + filename + "' не найден!";
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
