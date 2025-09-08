package org.commands;

import org.Static.CommandManager;
import org.classes.Movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/*

 */
public class ExecuteCommand implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        String[] commandParts = s.split("\\s+");
        if (commandParts.length < 2) {
            System.out.println("You must enter a script file name.");
            return false;
        }
        String filename = commandParts[1];
        Path scriptPath = Paths.get("./scripts/" + filename);

        if (CommandManager.fileQueue.contains(filename)) {
            System.out.println("Recursively executing!");
            return false;
        }

        try {
            CommandManager.fileQueue.add(filename);

            Files.lines(scriptPath).filter(line -> !line.trim().isEmpty()).forEach(line -> {
                String[] lineParts = line.trim().split("\\s+");
                String commandName = lineParts[0];

                Command command = CommandManager.listOfCommand.get(commandName);

                if (command == null) {
                    System.out.println("No such command!");
                } else {
                    System.out.println("\nExecuting: " + line);
                    command.doo(mySet, line);
                }
            });

            return true;
        } catch (IOException e) {
            System.out.println("Wrong script file!");
            return false;
        } finally {
            CommandManager.fileQueue.remove(filename);
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