package org.inner.commands;

import org.data.Movie;

import java.util.ArrayList;

/*

 */
public class FilterContainsName implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        if (s.split(" ").length == 1) {
            return "Please enter the name of the movie";
        }
        StringBuilder builder = new StringBuilder();

        if (mySet.size() != 0) {
            for (Movie movie : mySet) {//перебор во всех фильмах
                if (movie.getName().contains(s.split(" ")[1])) {
                    builder.append(movie.getName() + " contains " + s+"\n");
                }
            }
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
