package org.commands;

import org.classes.Movie;

import java.util.ArrayList;

/*

 */
public class PrintUniqueBudget implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        if (mySet.isEmpty()) {
            System.out.println("The collection is empty. No budgets to display.");
            return true;
        }

        System.out.println("Unique budget values found in the collection:");

        // Используем Stream API для получения и вывода уникальных значений бюджета.
        // Это декларативно, эффективно и лаконично.
        mySet.stream()                               // 1. Получаем поток (Stream) из коллекции Movie
                .map(Movie::getBudget)                  // 2. Преобразуем (map) каждый Movie в его бюджет (Float)
                .distinct()                         // 3. Оставляем в потоке только уникальные значения
                .sorted()
                .forEach(System.out::println);          // 4. Выводим каждое уникальное значение на консоль

        return true;
    }

    // Вспомогательный метод isInArray больше не нужен.
    // Его логику гораздо эффективнее выполняет операция .distinct()

    @Override
    public String des() {
        return "print_unique_budget : вывести уникальные значения поля budget всех элементов в коллекции";
    }

    @Override
    public String getName() {
        return "print_unique_budget";
    }
}