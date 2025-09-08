package org.inner.commands;

import org.data.inner.Movie;

import java.util.ArrayList;

/*

 */
public class ClearCommand implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        /*XMLManager.dropAll();*/
        mySet.clear();
        return new String("data is dropped!");
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
