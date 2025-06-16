package org.commands;

import org.classes.Movie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/*

 */
public class ReorderCommand implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        // Sorts `mySet` itself by movie name, ascending (A→Z)
        //mySet.sort(Comparator.comparing(Movie::getName));
        Collections.reverse(mySet);

        System.out.println("List is reordered:");
        InfoCommand info = new InfoCommand();
        info.doo(mySet, s);

        return true;
    }

    @Override
    public String des() {
        return "reorder : отсортировать коллекцию в порядке, обратном нынешнему";
    }

    @Override
    public String getName() {
        return "reorder";
    }
}
