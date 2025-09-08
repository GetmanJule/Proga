package org.Static;

import org.commands.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/*
 сервер при ответе клиенту отправляет актуальный список команд,
 клиент их хранит где-нибудь(местный хелп)
 */
public class CommandManager {
    public static Map<String, Command> listOfCommand;
    public static LinkedList<String> fileQueue = new LinkedList<>();

    static
    {
        listOfCommand = new LinkedHashMap<>();
        listOfCommand.put("help", new HelpCommand());
        listOfCommand.put("info", new InfoCommand());
        listOfCommand.put("show", new ShowCommand());
        listOfCommand.put("clear", new ClearCommand());
        listOfCommand.put("save", new SaveCommand());//перенести в апи сервера
        listOfCommand.put("filter_contains_name", new FilterContainsName());
        listOfCommand.put("filter_starts_with_name", new FilterStartsWithName());
        listOfCommand.put("print_unique_budget", new PrintUniqueBudget());
        listOfCommand.put("reorder", new ReorderCommand());

        listOfCommand.put("add", new AddElement());
        listOfCommand.put("remove_by_id", new RemoveByIdCommand());
        listOfCommand.put("remove_first", new RemoveFirstCommand());
        listOfCommand.put("remove_greater", new RemoveGreaterElement());
        listOfCommand.put("update", new UpdateCommand());

        listOfCommand.put("execute_script", new ExecuteCommand());
    }

    public static Map<String, Command> listOfNewCommand;

}
