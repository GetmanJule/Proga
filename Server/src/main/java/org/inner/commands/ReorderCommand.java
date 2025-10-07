package org.inner.commands;

import org.data.inner.Movie;

import java.util.Collections;
import java.util.List;

/*

 */
public class ReorderCommand implements Command {
    @Override
    public String doo(List<Movie> mySet, String s) {
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
