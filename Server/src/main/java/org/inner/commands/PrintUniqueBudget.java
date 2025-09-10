package org.inner.commands;

import org.data.inner.Movie;

import java.util.ArrayList;


public class PrintUniqueBudget implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        ArrayList<Float> tmp = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        if (!mySet.isEmpty()) {
            mySet.stream()
                    .filter(movie -> !isInArray(tmp, movie.getBudget()))
                    .forEach(movie -> {
                        builder.append(movie.getId())
                                .append(" is unique: ")
                                .append(movie.getBudget())
                                .append("\n");
                        tmp.add(movie.getBudget());
                    });
        }

        return builder.toString();
    }

    public boolean isInArray(ArrayList<Float> mySet, float x) {
        for (int i = 0; i < mySet.size(); i++) {
            if (mySet.get(i) == x) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String des() {
        return "print_unique_budget : вывести уникальные значения поля budget всех элементов в коллекции";
    }

    @Override
    public String getName() {
        return "print_unique_budget";
    }
}
