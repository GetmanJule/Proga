package org.inner.commands;

import org.data.inner.Movie;
import org.inner.MovieRepository;

import java.util.List;

/**
 * Команда для сохранения коллекции фильмов в базу данных
 */
public class SaveCommand {

    /**
     * Сохраняет переданный список фильмов в БД.
     * Для новых фильмов вызывает add, для существующих — update.
     */
    public boolean doo(List<Movie> mySet) {
        if (mySet == null || mySet.isEmpty()) {
            System.out.println("Коллекция пуста, сохранять нечего.");
            return false;
        }
        MovieRepository.saveAll(mySet);
        System.out.println("Данные сохранены в базу данных!");
        return true;
    }

}
