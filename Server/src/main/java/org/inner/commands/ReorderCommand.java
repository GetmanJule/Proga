package org.inner.commands;

import org.data.inner.Movie;
import org.inner.utils.XMLManager;

import java.util.ArrayList;
import java.util.Collections;

/*

 */
public class ReorderCommand implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        Collections.reverse(mySet);
        new SaveCommand().doo(mySet);
        InfoCommand info = new InfoCommand();
        return "List is reordered:" + info.doo(mySet, s);
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
