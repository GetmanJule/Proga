package org.commands;

import org.classes.Movie;

import java.util.ArrayList;

/*

 */
public class ClearCommand implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        /*XMLManager.dropAll();*/
        mySet.clear();
        System.out.println("data is dropped!");
        return false;
    }

    @Override
    public String des() {
        return "clear : очистить коллекцию";
    }

    @Override
    public String getName() {
        return "clear";
    }
}
