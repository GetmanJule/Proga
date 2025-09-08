package org.commands;

import org.classes.Movie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

/*

 */
public class RemoveByIdCommand implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        // Шаг 1: Валидация и безопасное преобразование ID
        String[] commandParts = s.split("\\s+");
        if (commandParts.length < 2) {
            System.out.println("Please enter the ID of the movie to remove.");
            return true;
        }

        long targetId;
        try {
            // Используем Long.parseLong, так как ID в классе Movie - это long
            targetId = Long.parseLong(commandParts[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a valid number.");
            return true;
        }

        // Шаг 2: Используем Stream API для поиска элемента
        // findFirst() возвращает Optional<Movie>, который элегантно обрабатывает случай, когда элемент не найден.
        Optional<Movie> movieToRemove = mySet.stream()
                .filter(movie -> movie.getId() == targetId) // Находим фильм с нужным ID
                .findFirst();                               // Берем первый найденный (ID уникальны, так что он будет единственным)

        // Шаг 3: Обрабатываем результат поиска с помощью Optional
        if (movieToRemove.isPresent()) {
            // Если фильм найден...
            Movie movie = movieToRemove.get(); // ...извлекаем его из Optional
            mySet.remove(movie);               // ...безопасно удаляем из коллекции (мы не в цикле!)

            System.out.println("Removed '" + movie.getName() + "' (ID: " + movie.getId() + ") from the data.");

            // Опционально: пересортировка коллекции после удаления
            mySet.sort(Comparator.comparing(Movie::getNameUpperCase));
        } else {
            // Если Optional пуст (фильм не найден)
            System.out.println("No movie found with ID: " + targetId);
        }

        return true;
    }

    @Override
    public String des() {
        return "remove_by_id id : удалить элемент из коллекции по его id";
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }
}