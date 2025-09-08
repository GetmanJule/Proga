package org.commands;

import org.classes.Movie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


public class AddElement implements Command {

    private final CreateMovie movieCreator;

    public AddElement() {
        // Создаем экземпляр нашего утилитарного класса один раз
        this.movieCreator = new CreateMovie();
    }

    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        Movie newMovie = null;

        // Определяем режим работы: из скрипта или интерактивный
        if (s.split(" ").length > 1) {
            System.out.println("Попытка добавить фильм из скрипта...");
            newMovie = movieCreator.createFromScript(s, mySet);
        } else {
            System.out.println("--- Начало добавления нового фильма в интерактивном режиме ---");
            // Передаем новый Scanner, чтобы не было конфликтов с основным циклом программы
            Scanner scanner = new Scanner(System.in);
            newMovie = movieCreator.createInteractively(scanner, mySet);
        }

        // Если фильм был успешно создан (не null), добавляем его и сортируем коллекцию
        if (newMovie != null) {
            mySet.add(newMovie);
            // Сортируем коллекцию по имени после добавления нового элемента
            mySet.sort(Comparator.comparing(Movie::getNameUpperCase));
            System.out.println("Фильм успешно добавлен в коллекцию!");
        } else {
            System.out.println("Операция добавления фильма была отменена или завершилась с ошибкой.");
        }
        return true; // Команда всегда считается выполненной, чтобы программа не завершалась
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