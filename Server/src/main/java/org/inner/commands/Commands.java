package org.inner.commands;

import org.data.inner.Movie;

import java.util.ArrayList;

/**
 * Coommands
 */

public class Commands {

    public String commandsEditor(ArrayList<Movie> mySet, String line, Movie arg) throws NullPointerException {
        String[] cmdStr = line.split(" ");
        try {
            Command command = CommandManager.listOfCommand.get(cmdStr[0]);
            if (arg != null) arg.setId(arg.hashCode());
            command.setArg(arg);
            return command.doo(mySet, line);
        } catch (NullPointerException e) {
            return "Команда не найдена!";
        }
    }
}