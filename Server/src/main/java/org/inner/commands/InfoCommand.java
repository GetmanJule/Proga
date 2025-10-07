package org.inner.commands;

import org.data.inner.Movie;

import java.util.List;

/*

 */
public class InfoCommand implements Command {
    @Override
    public String doo(List<Movie> mySet, String s) {
        StringBuilder builder = new StringBuilder();
        builder.append("Количество Объектов:  " + mySet.size() + "\n");
        if (mySet.size() > 0) {
            mySet.stream().peek(movie -> builder.append(movie.getName() + ", " + movie.getId() + "\n")).toList();
        }
        return builder.toString();
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
