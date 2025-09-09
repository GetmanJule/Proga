package org.inner.commands;

import org.data.inner.Movie;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Команда для добавления нового элемента Movie
 */
public class AddElement implements Command {

    private Movie arg; // объект, который приходит с клиента

    @Override
    public void setArg(Movie arg) {
        this.arg = arg;
    }

    @Override
    public String doo(ArrayList<Movie> mySet, String s) {

    if (arg == null) {
            return "Ошибка: не передан объект Movie для добавления!";
        }

        // Проверка уникальности passportId оператора
        if (arg.getOperator() != null) {
            for (Movie m : mySet) {
                if (m.getOperator().getPassportID().equals(arg.getOperator().getPassportID())) {
                    return "Ошибка: Person passportID '" + arg.getOperator().getPassportID() + "' уже существует!";
                }
            }
        }

        // Добавляем в коллекцию
        mySet.add(arg);
        mySet.sort(Comparator.comparing(Movie::getNameUpperCase));

        return "Фильм '" + arg.getName() + "' успешно добавлен!";
    }

    @Override
    public String des() {
        return "add {element} : добавить новый элемент в коллекцию";
    }

    @Override
    public String getName() {
        return "add";
    }
}
