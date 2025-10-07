package org.inner.commands;

import org.data.inner.Movie;
import org.inner.MovieRepository;

import java.util.List;

public class AddElement implements Command {

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
        if (arg == null) return "Ошибка: не передан объект Movie для добавления!";

        // Проверка уникальности passportId
        if (arg.getOperator() != null) {
            for (Movie m : mySet) {
                if (m.getOperator() != null &&
                    m.getOperator().getPassportID().equals(arg.getOperator().getPassportID())) {
                    return "Ошибка: Person passportID '" + arg.getOperator().getPassportID() + "' уже существует!";
                }
            }
        }

        mySet.add(arg);

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
