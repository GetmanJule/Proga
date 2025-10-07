package org.inner.commands;

import org.data.inner.Movie;
import org.inner.MovieRepository;

import java.util.Iterator;
import java.util.List;

public class RemoveGreatherElement implements Command {

    private Movie arg;

    @Override
    public void setArg(Movie arg) {
        this.arg = arg;
    }

    @Override
    public String doo(List<Movie> collection, String s) {
        if (arg == null) return "Некорректный объект";
        if (collection.isEmpty()) return "Коллекция пуста.";

        StringBuilder result = new StringBuilder();
        Iterator<Movie> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Movie movie = iterator.next();
            if (movie.compareTo(arg) > 0) {
                iterator.remove(); // удаляем только из коллекции
                result.append("Фильм с id ").append(movie.getId())
                        .append(" ('").append(movie.getName()).append("') удалён.\n");
            }
        }

        return result.length() > 0 ? result.toString().trim() : "Фильмов больше элемента с id " + arg.getId() + " не найдено.";
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
