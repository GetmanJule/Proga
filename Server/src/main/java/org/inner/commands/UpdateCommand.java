package org.inner.commands;

import org.data.inner.Movie;
import org.inner.MovieRepository;

import java.util.List;

public class UpdateCommand implements Command {

    private Movie arg;

    @Override
    public void setArg(Movie arg) {
        this.arg = arg;
    }

    @Override
    public String doo(List<Movie> mySet, String s) {
        if (arg == null) return "Ошибка: объект для обновления не передан!";
        if (s == null || !s.matches("^update\\s+\\d+$")) return "Ошибка: используйте update {id}";

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
