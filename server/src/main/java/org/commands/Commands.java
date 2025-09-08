package org.commands;
import org.Static.CommandManager;
import org.classes.Movie;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Coommands
 */

public class Commands {

    public boolean commandsEditor(ArrayList<Movie> mySet, String line) throws IOException {
        String[] cmdStr = line.split(" ");
        CommandManager.listOfCommand.get(cmdStr[0]).doo(mySet, line); //ищет команду в commandManager и запускает
        return true;
    }
}