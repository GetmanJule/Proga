package org.inner.commands;

import org.data.Movie;

import java.util.ArrayList;

/*

 */
public class FilterStartsWithName implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        if (s.split(" ").length == 1) {
            return "Please enter the name of the movie";
        }
        StringBuilder builder = new StringBuilder();

        if (mySet.size() != 0) {
            for (Movie movie : mySet) {
                if (movie.getName().startsWith(s.split(" ")[1])) {
                    builder.append(movie.getName() + " start with " + s + "\n");
                }
            }
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
