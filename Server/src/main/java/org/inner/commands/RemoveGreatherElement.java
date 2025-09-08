package org.inner.commands;

import org.data.inner.Movie;

import java.util.ArrayList;
import java.util.Comparator;

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
        ArrayList<Movie> newMySet = new ArrayList<>();

        for (Movie m : mySet) {
            if (m.getId() >= id) {
                result.append("Фильм с id ").append(m.getId())
                        .append(" ('").append(m.getName()).append("') удалён.\n");
            } else {
                newMySet.add(m);
            }
        }

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
