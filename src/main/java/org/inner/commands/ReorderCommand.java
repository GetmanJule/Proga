package org.inner.commands;

import org.data.Movie;

import java.util.ArrayList;
import java.util.Collections;

/*

 */
public class ReorderCommand implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        // Sorts `mySet` itself by movie name, ascending (A→Z)
        //mySet.sort(Comparator.comparing(Movie::getName));
        Collections.reverse(mySet);
        InfoCommand info = new InfoCommand();
        info.doo(mySet, s);

        return "List is reordered:";
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
