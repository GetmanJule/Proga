package org.commands;

import org.classes.Movie;

import java.util.ArrayList;
import java.util.stream.Collectors;

/*

 */
public class InfoCommand implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        // Выводим общую информацию о коллекции
        System.out.println("Collection Type: " + mySet.getClass().getSimpleName()); // Получаем простое имя класса, например, "ArrayList"
        System.out.println("Number of Elements: " + mySet.size());

        // Используем Stream API для вывода содержимого, если коллекция не пуста
        if (!mySet.isEmpty()) {
            System.out.println("Elements (Name, ID):");

            // 1. Создаем поток из коллекции
            // 2. Преобразуем каждый объект Movie в строку формата " -> Имя, ID"
            // 3. Выводим каждую полученную строку на консоль
            mySet.stream()
                    .map(movie -> " -> " + movie.getName() + ", " + movie.getId())
                    .forEach(System.out::println);
        }

        return true;
    }

    @Override
    public String des() {
        return "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }

    @Override
    public String getName() {
        return "info";
    }
}