package org.inner.commands;

import org.data.inner.Movie;

import java.util.List;

/*

 */
public class FilterStartsWithName implements Command {
    @Override
    public String doo(List<Movie> mySet, String s) {
        if (s.split(" ").length == 1) {
            return "Please enter the name of the movie";
        }
        StringBuilder builder = new StringBuilder();
        List<Movie> movies = mySet.stream().filter(movie -> movie.getName().startsWith(s.split(" ")[1])).peek(movie -> builder.append(movie.getName() + " start with " + s + "\n")).toList();
        if (movies.size() != 0) {
            return builder.toString();
        } else {
            return "No movies found!";
        }
    }

    @Override
    public String des() {
        return "filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки";
    }

    @Override
    public String getName() {
        return "filter_starts_with_name";
    }
}
