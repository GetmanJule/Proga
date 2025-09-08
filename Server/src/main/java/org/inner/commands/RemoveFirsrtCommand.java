package org.inner.commands;

import org.data.Movie;

import java.util.ArrayList;
import java.util.Comparator;

/*

 */
public class RemoveFirsrtCommand implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        if (mySet.isEmpty()) {
            return "Коллекция пуста! Удалять нечего.";
        }

        Movie removed = mySet.remove(0);
        mySet.sort(Comparator.comparing(Movie::getNameUpperCase));

        return "Фильм '" + removed.getName() + "' удалён из коллекции.";
    }


    @Override
    public String des() {
        return "remove_first : удалить первый элемент из коллекции";
    }

    @Override
    public String getName() {
        return "remove_first";
    }
}
