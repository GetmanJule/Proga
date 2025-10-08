package org.inner.commands;

import org.data.inner.Movie;
import org.inner.MovieRepository;

import java.util.List;

public class RemoveByIdCommand implements Command {
    private String login;

    @Override
    public void setLogin(String login) {
        this.login = login;
    }
    @Override
    public String doo(List<Movie> mySet, String s) {
        if (s.split(" ").length < 2) return "Введите ID для удаления";

        int id;
        try {
            id = Integer.parseInt(s.split(" ")[1]);
        } catch (NumberFormatException e) {
            return "Некорректный ID: " + s.split(" ")[1];
        }

        Movie toRemove = mySet.stream().filter(m -> (m.getId() == id && m.getUserLogin().equals(login))).findFirst().orElse(null);
        if (toRemove == null) return "Фильм с ID " + id + " не найден или не принадлежит вам";

        // Удаляем из коллекции
        mySet.remove(toRemove);
        return "Фильм '" + toRemove.getName() + "' с ID " + id + " удалён.";
    }

    @Override
    public String des() {
        return "remove_by_id id : удалить элемент из коллекции по id";
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }
}
