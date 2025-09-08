package org.commands;

import org.classes.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*

 */
public class FilterStartsWithName implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        // Шаг 1: Корректная валидация и извлечение префикса
        String[] commandParts = s.split("\\s+", 2); // Разделяем на команду и ВСЕ остальное
        if (commandParts.length < 2 || commandParts[1].isEmpty()) {
            System.out.println("Please enter the name prefix to search for.");
            return true;
        }
        String namePrefix = commandParts[1];

        // Шаг 2: Проверка на пустую коллекцию
        if (mySet.isEmpty()) {
            System.out.println("The collection is empty. No movies found!");
            return true;
        }

        System.out.println("Searching for movies whose name starts with '" + namePrefix + "'...");

        // Шаг 3: Основная логика поиска с использованием Stream API
        // Фильтруем коллекцию и собираем все фильмы, начинающиеся с префикса, в новый список.
        List<Movie> foundMovies = mySet.stream()
                .filter(movie -> movie.getName().startsWith(namePrefix))
                .collect(Collectors.toList());

        // Шаг 4: Вывод результата на основе полученного списка
        if (foundMovies.isEmpty()) {
            System.out.println("No movies found starting with the specified prefix.");
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
        return "filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки";
    }

    @Override
    public String getName() {
        return "filter_starts_with_name";
    }
}