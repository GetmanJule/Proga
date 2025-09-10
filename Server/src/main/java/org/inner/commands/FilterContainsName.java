package org.inner.commands;

import org.data.inner.Movie;

import java.util.ArrayList;
import java.util.List;

/*

 */
public class FilterContainsName implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        if (s.split(" ").length == 1) {
            return "Please enter the name of the movie";
        }
        StringBuilder builder = new StringBuilder();
        List<Movie> movies = mySet.stream().filter(movie -> movie.getName().contains(s.split(" ")[1])).peek(movie -> builder.append(movie.getName() + " contains " + s + "\n")).toList();

        if (movies.size() != 0) {
            return builder.toString();
        } else {
            return "No movies found!";
        }
    }

    @Override
    public String des() {
        return "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку";
    }

    @Override
    public String getName() {
        return "filter_contains_name";
    }
}
