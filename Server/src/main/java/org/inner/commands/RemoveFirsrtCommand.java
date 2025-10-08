package org.inner.commands;

import org.data.inner.Movie;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/*

 */
public class RemoveFirsrtCommand implements Command {
    private String login;

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String doo(List<Movie> mySet, String s) {
        if (mySet.isEmpty()) {
            return "Коллекция пуста! Удалять нечего.";
        }
        Movie removed = removeFirstByOwner(mySet, login);
        if (removed==null){
            return "Не найдено записей для удаления для пользователя!";
        }
        mySet.sort(Comparator.comparing(Movie::getNameUpperCase));

        return "Фильм '" + removed.getName() + "' удалён из коллекции.";
    }


    @Override
    public String des() {
        return "remove_first : удалить первый элемент из коллекции";
    }

    @Override
    public String getName() {
        return "remove_first";
    }

    private Movie removeFirstByOwner(List<Movie> collection, String login) {
        for (Iterator<Movie> iterator = collection.iterator(); iterator.hasNext(); ) {
            Movie movie = iterator.next();
            if (login.equals(movie.getUserLogin())) {
                iterator.remove(); // безопасное удаление из списка
                return movie;       // нашли и удалили только первый
            }
        }
        return null; // ничего не нашли
    }

}
