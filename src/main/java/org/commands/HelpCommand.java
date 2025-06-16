package org.commands;

import org.Static.CommandManager;
import org.classes.Movie;

import java.util.ArrayList;

/*

 */
public class HelpCommand implements Command {

    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        String alldes = "";
        for(Command c: CommandManager.listOfCommand.values()) {
            /*String s = "\ninfo : информация о коллекции\nshow : все элементы коллекции в строковом представлении\nadd element_name : добавить новый элемент в коллекцию\nupdate id new_element : обновить значение элемента коллекции\nremove_by_id id : удалить элемент из коллекции по его id\nclear : очистить коллекцию\nsave : сохранить коллекцию в файл\nexecute_script file_name : считать и исполнить скрипт из указанного файла\nsum_of_annual_turnover : сумма значений поля annualTurnover для всех элементов коллекции\naverage_of_annual_turnover : среднее значение поля annualTurnover для всех элементов коллекции\nprint_descending : вывести элементы коллекции в порядке убывания\nchange_serialization_type JSON|CSV\nSerialization_type\n\nexit : завершить программу (без сохранения в файл)";
             */
            alldes = alldes + c.des() + "\n";
        }

        System.out.println(alldes);

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
