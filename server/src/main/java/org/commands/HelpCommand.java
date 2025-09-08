package org.commands;

import org.Static.CommandManager;
import org.classes.Movie;

import java.util.ArrayList;
import java.util.stream.Collectors;

/*

 */
public class HelpCommand implements Command {

    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        // Используем Stream API для преобразования коллекции команд в единую строку.
        // Это более производительно и читаемо, чем конкатенация строк в цикле.
        String allDescriptions = CommandManager.listOfCommand.values().stream() // 1. Получаем поток (Stream) из значений карты
                .map(Command::des)                                            // 2. Преобразуем (map) каждый объект Command в его описание (String)
                .collect(Collectors.joining("\n"));                       // 3. Собираем (collect) все строки в одну, разделяя их переносом строки

        System.out.println(allDescriptions);

        return true;
    }

    @Override
    public String des() {
        return "help : вывести справку по доступным командам";
    }

    @Override
    public String getName() {
        return "help";
    }
}