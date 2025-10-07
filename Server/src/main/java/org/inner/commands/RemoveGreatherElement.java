package org.inner.commands;

import org.data.inner.Movie;

import java.util.List;

/*

 */
public class RemoveGreatherElement implements Command {
    private Movie arg; //временный объект, который приходит с клиента

    @Override
    public void setArg(Movie arg) {
        this.arg = arg;
    }

    @Override
    public String doo(List<Movie> mySet, String s) {
        if (arg == null) {
            return "Некорректный объект";
        }

        if (mySet.isEmpty()) {
            return "Коллекция пуста, нечего удалять.";
        }
        StringBuilder result = new StringBuilder();

        Movie baseMovie = arg; //временная ссылка на пришедший объект
        List<Movie> newMySet = mySet.stream()
                .peek(m -> {
                    if (m.compareTo(baseMovie) < 0) {
                        result.append("Фильм с id ")
                                .append(m.getId())
                                .append(" ('")
                                .append(m.getName())
                                .append("') удалён.\n");
                    }
                })
                .filter(m -> m.compareTo(baseMovie) >= 0)
                .toList();

        mySet.clear();
        mySet.addAll(newMySet);

        if (result.length() == 0) {
            return "Фильмов больше элемента с id " + arg.getId() + " не найдено.";
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
