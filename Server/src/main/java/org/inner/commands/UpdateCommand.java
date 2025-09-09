package org.inner.commands;

import org.data.inner.Movie;

import java.util.ArrayList;
import java.util.Comparator;

public class UpdateCommand implements Command {

    private Movie arg; // объект, который приходит с клиента

    @Override
    public void setArg(Movie arg) {
        this.arg = arg;
    }

    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        // Проверка, что пришёл аргумент

        if (arg == null) {
            return "Ошибка: объект для обновления не передан!";
        }

        // Проверка, что передан id в команде
        if (s == null || !s.matches("^update\\s+\\d+$")) {
            return "Ошибка: некорректный ввод! Используйте: update {id}";
        }

        long id;
        try {
            id = Long.parseLong(s.split(" ")[1]);
        } catch (NumberFormatException e) {
            return "Ошибка: id должен быть числом!";
        }

        // Находим объект по id
        Movie existing = null;
        for (Movie m : mySet) {
            if (m.getId() == id) {
                existing = m;
                break;
            }
        }

        if (existing == null) {
            return "Объект с id " + id + " не найден!";
        }

        // Заменяем поля существующего объекта на поля пришедшего
        existing.setName(arg.getName());
        existing.setCoordinates(arg.getCoordinates());
        existing.setOscarsCount(arg.getOscarsCount());
        existing.setBudget(arg.getBudget());
        existing.setUsaBoxOffice(arg.getUsaBoxOffice());
        existing.setMpaaRating(arg.getMpaaRating());
        existing.setOperator(arg.getOperator());

        // Сортировка коллекции по имени
        mySet.sort(Comparator.comparing(Movie::getNameUpperCase));

        return "Объект с id " + id + " успешно обновлен!";
    }

    @Override
    public String des() {
        return "update {id} {element} : обновить объект коллекции по id с данными, присланными с клиента";
    }

    @Override
    public String getName() {
        return "update";
    }
}
