package org.inner.commands;

import org.data.inner.Coordinates;
import org.data.inner.Location;
import org.data.inner.Movie;
import org.data.inner.Person;
import org.data.inner.enums.Color;
import org.data.inner.enums.Country;
import org.data.inner.enums.MpaaRating;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/*

 */
public class ExecuteCommand implements Command {
    @Override
    public String doo(List<Movie> mySet, String s) {
        String[] idS = s.split(" ");
        String filename;
        try {
            filename = idS[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Путь не указан! Попробуйте еще раз!";
        }

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
                        Command command = CommandManager.listOfCommand.get(commandName);
                        try {
                            Movie movie = getMovie(line);
                            command.setArg(movie);
                        }catch (ArrayIndexOutOfBoundsException e){
                        }


                        String output = command.doo(mySet, line);
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

    private static Movie getMovie(String line) {
        String[] all = line.split(" ");
        Movie movie = new Movie(all[1], new Coordinates(Float.parseFloat(all[2]), Long.parseLong(all[3])),
                Long.parseLong(all[4]), Float.parseFloat(all[5]), Double.parseDouble(all[6]), MpaaRating.getRating(Integer.parseInt(all[7])),
                new Person(all[8], all[9], Color.getColorByValue(Integer.parseInt(all[10])), Country.getCountryByValue(Integer.parseInt(all[11])),
                        new Location(Float.parseFloat(all[12]), Double.parseDouble(all[13]), all[14])));
        return movie;
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
