package org.commands;

import org.classes.Movie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

/*

 */
public class UpdateCommand implements Command {

    private final CreateMovie movieCreator;

    public UpdateCommand() {
        // Команда 'update' зависит от 'CreateMovie', чтобы не дублировать код.
        // Мы создаем экземпляр один раз в конструкторе.
        this.movieCreator = new CreateMovie();
    }

    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        // Шаг 1: Валидация и безопасное получение ID
        String[] commandParts = s.split("\\s+");
        if (commandParts.length < 2) {
            System.out.println("Please enter the ID of the movie to update.");
            return true;
        }

        long targetId;
        try {
            targetId = Long.parseLong(commandParts[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a valid number.");
            return true;
        }

        // Шаг 2: Поиск обновляемого фильма с помощью Stream API и Optional
        Optional<Movie> movieToUpdateOptional = mySet.stream()
                .filter(movie -> movie.getId() == targetId)
                .findFirst();

        // Шаг 3: Обработка результата поиска
        // Используем ifPresentOrElse для красивой обработки обоих случаев: "найдено" и "не найдено"
        movieToUpdateOptional.ifPresentOrElse(
                // --- Лямбда, которая выполняется, ЕСЛИ фильм НАЙДЕН ---
                movieToUpdate -> {
                    System.out.println("Found movie to update: '" + movieToUpdate.getName() + "'. Please enter new data.");

                    // --- ПОВТОРНОЕ ИСПОЛЬЗОВАНИЕ КОДА ---
                    // Вызываем наш уже существующий CreateMovie для получения новых данных от пользователя.
                    // Это избавляет от 200 строк дублирующегося кода!
                    Scanner scanner = new Scanner(System.in);
                    Movie newData = movieCreator.createInteractively(scanner, mySet);

                    // --- Корректная проверка уникальности PassportID ---
                    // Проверяем, не занят ли новый PassportID ДРУГИМ фильмом в коллекции.
                    String newPassportId = newData.getOperator().getPassportID();
                    boolean isDuplicate = mySet.stream()
                            // Исключаем из проверки сам обновляемый фильм
                            .filter(m -> m.getId() != targetId)
                            // Ищем совпадения среди остальных
                            .anyMatch(m -> m.getOperator().getPassportID().equals(newPassportId));

                    if (isDuplicate) {
                        System.out.println("Error: The new Passport ID is already used by another movie. Update cancelled.");
                        return; // Прерываем операцию
                    }

                    // --- Применение новых данных ---
                    // Мы не заменяем объект, а обновляем его поля, сохраняя исходный ID и дату создания.
                    movieToUpdate.setName(newData.getName());
                    movieToUpdate.setCoordinates(newData.getCoordinates());
                    movieToUpdate.setOscarsCount(newData.getOscarsCount());
                    movieToUpdate.setBudget(newData.getBudget());
                    movieToUpdate.setUsaBoxOffice(newData.getUsaBoxOffice());
                    movieToUpdate.setMpaaRating(newData.getMpaaRating());
                    movieToUpdate.setOperator(newData.getOperator());

                    System.out.println("Object with id " + targetId + " has been successfully updated!");

                    // Сортируем коллекцию после обновления
                    mySet.sort(Comparator.comparing(Movie::getNameUpperCase));
                },
                // --- Лямбда, которая выполняется, ЕСЛИ фильм НЕ НАЙДЕН ---
                () -> {
                    System.out.println("Object with id " + targetId + " not found!");
                }
        );

        return true;
    }

    @Override
    public String des() {
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public String getName() {
        return "update";
    }
}