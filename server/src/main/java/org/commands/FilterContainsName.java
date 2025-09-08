package org.commands;

import org.classes.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*

 */
public class FilterContainsName implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        // Шаг 1: Корректная валидация и извлечение аргумента
        String[] commandParts = s.split("\\s+", 2); // Разделяем на команду и ВСЕ остальное
        if (commandParts.length < 2 || commandParts[1].isEmpty()) {
            System.out.println("Please enter the name substring to search for.");
            return true;
        }
        String nameSubstring = commandParts[1];

        // Шаг 2: Проверка на пустую коллекцию (выполняется до основной логики)
        if (mySet.isEmpty()) {
            System.out.println("The collection is empty. No movies found!");
            return true;
        }

        System.out.println("Searching for movies containing '" + nameSubstring + "' in their name...");

        // Шаг 3: Основная логика поиска с использованием Stream API
        // Фильтруем коллекцию и собираем все найденные фильмы в новый список.
        List<Movie> foundMovies = mySet.stream()
                .filter(movie -> movie.getName().contains(nameSubstring))
                .collect(Collectors.toList());

        // Шаг 4: Вывод результата на основе полученного списка
        if (foundMovies.isEmpty()) {
            System.out.println("No movies found with the specified name substring.");
        } else {
            System.out.println("Found " + foundMovies.size() + " movie(s):");
            // Используем forEach для вывода каждого найденного элемента
            foundMovies.forEach(movie ->
                    System.out.println(" -> " + movie) // Выводим полную информацию о фильме
            );
        }

        return true;
    }

    @Override
    public String des() {
        return "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку";
    }

    @Override
    public String getName() {
        return "filter_contains_name";
    }
}