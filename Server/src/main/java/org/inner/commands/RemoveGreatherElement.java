package org.inner.commands;

import org.data.inner.Movie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/*

 */
public class RemoveGreatherElement implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        String[] idS = s.split(" ");
        long id;

        try {
            id = Long.parseLong(idS[1]);
        } catch (Exception e) {
            return "Id should be a number!";
        }

        if (mySet.isEmpty()) {
            return "Коллекция пуста, нечего удалять.";
        }

        StringBuilder result = new StringBuilder();
        List<Movie> newMySet;

        newMySet = mySet.stream()
                .peek(m -> {
                    if (m.getId() >= id) {
                        result.append("Фильм с id ")
                                .append(m.getId())
                                .append(" ('").append(m.getName())
                                .append("') удалён.\n");
                    }
                })
                .filter(m -> m.getId() < id) // оставляем только нужные
                .toList();
        mySet.clear();
        mySet.addAll(newMySet);
        mySet.sort(Comparator.comparing(Movie::getNameUpperCase));

        if (result.length() == 0) {
            return "Фильмов с id >= " + id + " не найдено.";
        } else {
            return result.toString().trim();
        }
    }


    @Override
    public String des() {
        return "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный";
    }

    @Override
    public String getName() {
        return "remove_greater";
    }
}
