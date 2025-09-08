package org.commands;

import org.classes.Movie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Команда для удаления из коллекции всех элементов, превышающих заданный.
 * Сравнение происходит по "естественному порядку" (в данном случае - по id).
 */
public class RemoveGreaterElement implements Command {

    private final CreateMovie movieCreator;

    public RemoveGreaterElement() {
        this.movieCreator = new CreateMovie();
    }

    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        if (mySet.isEmpty()) {
            System.out.println("Коллекция пуста. Нечего удалять.");
            return true;
        }

        System.out.println("--- Создание элемента для сравнения ---");
        System.out.println("Элементы, которые больше (по ID), чем созданный, будут удалены.");

        Movie elementToCompare = null;

        // Логика аналогична команде 'add': создаем элемент либо из скрипта, либо интерактивно.
        if (s.split(" ").length > 1) {
            elementToCompare = movieCreator.createFromScript(s, mySet);
        } else {
            Scanner scanner = new Scanner(System.in);
            elementToCompare = movieCreator.createInteractively(scanner, mySet);
        }

        // Если создание элемента не удалось (например, из-за ошибки в скрипте), выходим.
        if (elementToCompare == null) {
            System.out.println("Не удалось создать элемент для сравнения. Операция отменена.");
            return true;
        }

        System.out.println("Создан временный элемент для сравнения с ID: " + elementToCompare.getId());
        System.out.println("Начинается процесс удаления...");

        int removedCount = 0;

        // Используем итератор для безопасного удаления элементов во время перебора коллекции
        Iterator<Movie> iterator = mySet.iterator();
        while (iterator.hasNext()) {
            Movie movieInCollection = iterator.next();

            // Если текущий элемент в коллекции больше, чем наш временный элемент
            if (movieInCollection.compareTo(elementToCompare) > 0) {
                // Выводим информацию об удаляемом элементе
                System.out.println("Удаляется элемент -> ID: " + movieInCollection.getId() + ", Имя: '" + movieInCollection.getName() + "'");

                // Безопасно удаляем элемент с помощью итератора
                iterator.remove();
                removedCount++;
            }
        }

        if (removedCount > 0) {
            System.out.println("Удаление завершено. Всего удалено элементов: " + removedCount);
        } else {
            System.out.println("Ни один элемент не был удален (не найдено элементов, превышающих заданный).");
        }

        return true;
    }

    @Override
    public String des() {
        return "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный";
    }

    @Override
    public String getName() {
        return "remove_greater";
    }
}