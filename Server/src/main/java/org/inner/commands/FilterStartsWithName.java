package org.inner.commands;

import org.data.inner.Movie;

import java.util.List;

/*

 */
public class FilterStartsWithName implements Command {
    @Override
    public String doo(List<Movie> mySet, String s) {
        String[] parts = s.split(" ", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            return "Please enter the name of the movie";
        }

        String search = parts[1].toLowerCase();
        StringBuilder builder = new StringBuilder();

        List<Movie> movies = mySet.stream()
                .filter(movie -> movie.getName() != null &&
                                 movie.getName().toLowerCase().startsWith(search))
                .peek(movie -> builder.append("Movie '")
                        .append(movie.getName())
                        .append("' starts with '")
                        .append(search)
                        .append("'\n"))
                .toList();

        return movies.isEmpty()
                ? "No movies found!"
                : builder.toString();
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
