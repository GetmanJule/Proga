package org.commands;

import org.classes.Movie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

/*

 */
public class RemoveFirstCommand implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        // Шаг 1: Проверяем, пуста ли коллекция.
        if (mySet.isEmpty()) {
            System.out.println("The collection is empty. Nothing to remove.");
            return true;
        }

        // Шаг 2: Используем Stream API для безопасного получения первого элемента.
        // .findFirst() возвращает Optional<Movie>, что защищает нас от NullPointerException.
        Optional<Movie> movieToRemove = mySet.stream().findFirst();

        // Шаг 3: Используем .ifPresent() для выполнения действий, если элемент существует.
        // Это красивый и безопасный функциональный подход.
        // Так как мы уже проверили, что коллекция не пуста, этот код гарантированно выполнится.
        movieToRemove.ifPresent(movie -> {
            // Безопасно удаляем найденный объект (мы не находимся внутри цикла)
            mySet.remove(movie);
            System.out.println("Removed the first element: '" + movie.getName() + "'");

            // Опционально: пересортировка коллекции после удаления
            mySet.sort(Comparator.comparing(Movie::getNameUpperCase));
        });

        return true;
    }

    @Override
    public String des() {
        return "remove_first : удалить первый элемент из коллекции";
    }

    @Override
    public String getName() {
        return "remove_first";
    }
}