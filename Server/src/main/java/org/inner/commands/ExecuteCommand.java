package org.inner.commands;

import org.data.inner.*;
import org.data.inner.enums.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * execute_script file_name — выполнить команды из указанного файла
 */
public class ExecuteCommand implements Command {

    private String login;

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String doo(List<Movie> mySet, String s) {
        String[] args = s.trim().split(" ");
        if (args.length < 2) {
            return " Путь к файлу не указан!";
        }

        String filename = args[1];
        File file = new File("./scripts/" + filename);
        if (!file.exists()) {
            return "Файл '" + filename + "' не найден!";
        }

        // предотвращаем рекурсию
        if (CommandManager.fileQueue.contains(filename)) {
            return " Обнаружена рекурсия при выполнении '" + filename + "'";
        }

        CommandManager.fileQueue.add(filename);
        StringBuilder result = new StringBuilder("Выполнение скрипта '" + filename + "':\n");

        List<String> lines = new ArrayList<>();
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (!line.isEmpty()) lines.add(line);
            }
        } catch (FileNotFoundException e) {
            return " Не удалось прочитать файл '" + filename + "'";
        }

        // 1️⃣ Собираем все корректные команды
        List<Command> validCommands = new ArrayList<>();
        List<String> validLines = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(" ");
            String cmdName = parts[0];

            if (cmdName.equals("execute_script")) {
                String nestedFile = parts.length > 1 ? parts[1] : "";
                if (CommandManager.fileQueue.contains(nestedFile)) {
                    result.append("️ Пропущено из-за рекурсии: ").append(nestedFile).append("\n");
                    continue;
                }
            }

            Command cmd = CommandManager.listOfCommand.get(cmdName);
            if (cmd != null) {
                validCommands.add(cmd);
                validLines.add(line);
            } else {
                result.append("Неизвестная команда: ").append(cmdName).append("\n");
            }
        }

        for (int i = 0; i < validCommands.size(); i++) {
            Command cmd = validCommands.get(i);
            String line = validLines.get(i);
            cmd.setLogin(login);

            try {
                Movie movie = getMovie(line);
                cmd.setArg(movie);
            } catch (Exception ignored) {
                // если команда без аргументов — пропускаем
            }
        }

        for (int i = 0; i < validCommands.size(); i++) {
            Command cmd = validCommands.get(i);
            String line = validLines.get(i);
            result.append("Message! ").append(line).append("\n");

            try {
                String output = cmd.doo(mySet, line);
                result.append(output).append("\n");
            } catch (Exception e) {
                result.append("Ошибка при выполнении '").append(line).append("': ")
                        .append(e.getMessage()).append("\n");
            }
        }

        CommandManager.fileQueue.remove(filename);
        result.append("Скрипт  выполнен").append(filename).append("' завершён.\n");
        return result.toString();
    }

    private static Movie getMovie(String line) {
        String[] all = line.split(" ");
        if (all.length < 15) throw new IllegalArgumentException("Недостаточно аргументов для Movie");

        return new Movie(
                all[1],
                new Coordinates(Float.parseFloat(all[2]), Long.parseLong(all[3])),
                Long.parseLong(all[4]),
                Float.parseFloat(all[5]),
                Double.parseDouble(all[6]),
                MpaaRating.getRating(Integer.parseInt(all[7])),
                new Person(
                        all[8],
                        all[9],
                        Color.getColorByValue(Integer.parseInt(all[10])),
                        Country.getCountryByValue(Integer.parseInt(all[11])),
                        new Location(
                                Float.parseFloat(all[12]),
                                Double.parseDouble(all[13]),
                                all[14]
                        )
                )
        );
    }

    @Override
    public String des() {
        return "execute_script file_name : выполнить команды из указанного файла";
    }

    @Override
    public String getName() {
        return "execute_script";
    }
}
