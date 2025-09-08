package org.inner.commands;


import org.data.inner.Movie;

import java.util.ArrayList;

/*

 */
public class ShowCommand implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        StringBuilder builder = new StringBuilder();
        if (mySet.size() > 0) {
            for (Movie m : mySet) {
                builder.append(m);
            }
            return builder.toString();
        } else {
            return "data is empty!";
        }
    }

    @Override
    public String des() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }

    @Override
    public String getName() {
        return "show";
    }
}
