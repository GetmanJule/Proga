package org.inner.commands;

import org.data.Movie;

import java.util.ArrayList;
import java.util.Comparator;

/*

 */
public class RemoveByIdCommand implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        if (s.split(" ").length == 1) {
            return "Please enter the ID of the movie";
        }

        String idStr = s.split(" ")[1];
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            return "Invalid ID format: " + idStr;
        }

        if (mySet.isEmpty()) {
            return "Коллекция пуста!";
        }

        Movie toRemove = null;
        for (Movie m : mySet) {
            if (m.getId() == id) {
                toRemove = m;
                break;
            }
        }

        if (toRemove != null) {
            mySet.remove(toRemove);
            mySet.sort(Comparator.comparing(Movie::getNameUpperCase));
            return "Фильм '" + toRemove.getName() + "' с ID " + id + " успешно удалён.";
        } else {
            return "Фильм с ID " + id + " не найден.";
        }
    }


    @Override
    public String des() {
        return "remove_by_id id : удалить элемент из коллекции по его id";
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }
}
