package org.inner.commands;

import org.data.Movie;

import java.util.ArrayList;

/*

 */
public class InfoCommand implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        String al = "";
        if (mySet.size() > 0) {
            for (Movie movie : mySet) {
                al = al + movie.getName() + ", " + movie.getId() + "\n";
            }
            return al;
        }

        return "Количество Объектов:  " + mySet.size();
    }

    @Override
    public String des() {
        return "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }

    @Override
    public String getName() {
        return "info";
    }
}
