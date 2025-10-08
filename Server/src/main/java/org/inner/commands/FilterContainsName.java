package org.inner.commands;

import org.data.inner.Movie;

import java.util.List;

/*

 */
public class FilterContainsName implements Command {
    @Override
    public String doo(List<Movie> mySet, String s) {
        String[] parts = s.split(" ", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            return "Please enter the name of the movie";
        }

        String search = parts[1].toLowerCase();
        StringBuilder builder = new StringBuilder();

        List<Movie> movies = mySet.stream()
                .filter(movie -> movie.getName() != null && movie.getName().toLowerCase().contains(search))
                .peek(movie -> builder.append("Movie '")
                        .append(movie.getName())
                        .append("' contains '")
                        .append(search)
                        .append("'\n"))
                .toList();

        if (!movies.isEmpty()) {
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
