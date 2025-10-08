package org.inner.commands;

import org.data.inner.Movie;

import java.util.List;
import java.util.Optional;

public class UpdateCommand implements Command {

    private Movie arg;
    private String login;

    @Override
    public void setLogin(String login) {
        this.login = login;
    }


    @Override
    public void setArg(Movie arg) {
        this.arg = arg;
    }

    @Override
    public String doo(List<Movie> mySet, String s) {
        if (arg == null) return "Ошибка: объект для обновления не передан!";
        if (s == null || !s.matches("^update\\s+\\d+$")) return "Ошибка: используйте update {id}";
        Movie oldMovie = mySet.stream().filter(movie -> movie.getId() == arg.getId()).findFirst().orElse(null);
        if (oldMovie == null) {
            return "Такого элемента для обновления нету!";
        }
        if (!login.equals(oldMovie.getUserLogin())) {
            return "Ошибка доступа, нет прав на изменение данного объекта, объект принадлежит " + oldMovie.getUserLogin();
        }
        long id;
        try {
            id = Long.parseLong(s.split(" ")[1]);
        } catch (NumberFormatException e) {
            return "Ошибка: id должен быть числом!";
        }

        Movie existing = mySet.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
        if (existing == null) return "Объект с id " + id + " не найден!";

        // Обновляем БД
        arg.setId(id);

        // Обновляем локальную коллекцию
        int index = mySet.indexOf(existing);
        mySet.set(index, arg);

        return "Объект с id " + id + " успешно обновлен!";
    }

    @Override
    public String des() {
        return "update {id} {element} : обновить объект коллекции по id";
    }

    @Override
    public String getName() {
        return "update";
    }
}
