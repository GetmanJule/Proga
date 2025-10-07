package org.inner.commands;

import org.data.inner.Movie;

import java.util.List;

public class ClearCommand implements Command {

    private String login;

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String doo(List<Movie> mySet, String s) {
        mySet.removeIf(m -> login.equals(m.getUserLogin()));
        return "Ваши элементы очищены! Оставшиеся: " + mySet.size();
    }

    @Override
    public String des() {
        return "clear : очистить только свои элементы (в коллекции)";
    }

    @Override
    public String getName() {
        return "clear";
    }
}
