package org.inner.commands;

import org.data.inner.Movie;

import java.util.ArrayList;

/**
 * Coommands
 */

public class Commands {

    public String commandsEditor(ArrayList<Movie> mySet, String line) throws NullPointerException {
        String[] cmdStr = line.split(" ");
        return  CommandManager.listOfCommand.get(cmdStr[0]).doo(mySet, line); //ищет команду в commandManager и запускает;
    }
}