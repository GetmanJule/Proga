package org.commands;

import org.classes.*;
import org.enums.Color;
import org.enums.Country;
import org.enums.MpaaRating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Утилитарный класс, отвечающий за создание объекта Movie.
 * Предоставляет методы для интерактивного создания и создания из строки (для скриптов).
 * Код интерактивного создания реорганизован с использованием функциональных интерфейсов.
 */
public class CreateMovie {

    public Movie createInteractively(Scanner scanner, ArrayList<Movie> existingMovies) {
        String name = askName(scanner);
        Coordinates coordinates = askCoordinates(scanner);
        long oscarsCount = askOscarsCount(scanner);
        float budget = askBudget(scanner);
        double usaBoxOffice = askUsaBoxOffice(scanner);
        MpaaRating mpaaRating = askMpaaRating(scanner);
        Person operator = askPerson(scanner, existingMovies);

        System.out.println("Фильм успешно создан!");
        return new Movie(name, coordinates, oscarsCount, budget, usaBoxOffice, mpaaRating, operator);
    }

    public Movie createFromScript(String args, ArrayList<Movie> existingMovies) {
        String[] all = args.split(" ");
        /*      0     1    2    3      4           5        6            7          8          9         10        11          12       13         14      */
        /* команда | name |corX|corY|oscarsCount|budget|usaBoxOffice|mpaaRating|persName|passportId|eyeColor|nationality|locX     |locY      |locName   */

        try {
            // Использование Stream API для проверки уникальности - это уже правильный подход.
            String passportId = all[9];
            boolean isDuplicateFound = existingMovies.stream()
                    .anyMatch(movie -> movie.getOperator().getPassportID().equals(passportId));
            if (isDuplicateFound) {
                System.out.println("Ошибка в скрипте: PassportID '" + passportId + "' уже существует.");
                return null;
            }

            return new Movie(all[1], new Coordinates(Float.parseFloat(all[2]), Long.parseLong(all[3])),
                    Long.parseLong(all[4]), Float.parseFloat(all[5]), Double.parseDouble(all[6]), MpaaRating.getRating(Integer.parseInt(all[7])),
                    new Person(all[8], passportId, Color.getColorByValue(Integer.parseInt(all[10])), Country.getCountryByValue(Integer.parseInt(all[11])),
                            new Location(Float.parseFloat(all[12]), Double.parseDouble(all[13]), all[14])));

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка в скрипте: недостаточно аргументов для создания фильма.");
            return null;
        } catch (Exception e) {
            System.out.println("Ошибка в скрипте: проверьте корректность типов данных.");
            return null;
        }
    }

    // --- Универсальный метод для запроса и валидации ввода ---

    /**
     * Универсальный метод для запроса и валидации пользовательского ввода.
     * @param scanner Сканер для чтения ввода.
     * @param prompt Сообщение-приглашение для пользователя.
     * @param parser Функция для преобразования строки в нужный тип (например, Float::parseFloat).
     * @param validator Предикат (условие) для проверки корректности значения (например, val -> val > 0).
     * @param <T> Тип данных, который мы хотим получить.
     * @return Валидное значение типа T.
     */
    private <T> T askForValidInput(Scanner scanner, String prompt, Function<String, T> parser, Predicate<T> validator) {
        System.out.println(prompt);
        while (true) {
            try {
                T value = parser.apply(scanner.nextLine());
                if (validator.test(value)) {
                    return value;
                }
            } catch (Exception e) {
                // Игнорируем ошибку парсинга, чтобы просто повторить запрос
            }
            System.out.println("Ввод некорректен. Пожалуйста, повторите попытку:");
        }
    }

    // --- Переработанные вспомогательные методы для интерактивного создания ---

    private String askName(Scanner sc) {
        // Для простых случаев, как этот, можно оставить while, но для единообразия используем наш новый метод
        return askForValidInput(sc,
                "Введите название фильма (непустая строка):",
                String::trim, // Парсер - просто убираем пробелы
                name -> !name.isEmpty() // Валидатор - проверяем, что не пусто
        );
    }

    private Coordinates askCoordinates(Scanner sc) {
        Float corX = askForValidInput(sc,
                "Введите координату X (число, max=906):",
                Float::parseFloat,
                x -> x <= 906
        );
        Long corY = askForValidInput(sc,
                "Введите координату Y (целое число, max=655):",
                Long::parseLong,
                y -> y <= 655
        );
        return new Coordinates(corX, corY);
    }

    private long askOscarsCount(Scanner sc) {
        return askForValidInput(sc,
                "Введите количество Оскаров (целое число > 0):",
                Long::parseLong,
                oscar -> oscar > 0
        );
    }

    private float askBudget(Scanner sc) {
        return askForValidInput(sc,
                "Введите бюджет фильма (число > 0):",
                Float::parseFloat,
                budget -> budget > 0
        );
    }

    private double askUsaBoxOffice(Scanner sc) {
        return askForValidInput(sc,
                "Введите кассовые сборы в США (число > 0):",
                Double::parseDouble,
                office -> office > 0
        );
    }

    private MpaaRating askMpaaRating(Scanner sc) {
        return askForValidInput(sc,
                "Выберите MPAA рейтинг:\n    1 - G\n    2 - PG\n    3 - PG_13\n    4 - R\n    5 - NC_17",
                s -> MpaaRating.getRating(Integer.parseInt(s)), // Парсер со вложенным парсером
                rating -> rating != null // Валидатор - проверяем, что такой enum существует
        );
    }

    private Person askPerson(Scanner sc, ArrayList<Movie> existingMovies) {
        System.out.println("--- Ввод данных оператора ---");
        String persName = askForValidInput(sc, "Введите имя оператора:", String::trim, name -> !name.isEmpty());

        String passportId = askForValidInput(sc,
                "Введите PassportID оператора (уникальная, непустая строка):",
                String::trim,
                id -> !id.isEmpty() && existingMovies.stream().noneMatch(m -> m.getOperator().getPassportID().equals(id)) // Сложный валидатор
        );

        Color eyeColor = askEyeColor(sc); // Поле может быть null, обрабатывается отдельно
        Country nationality = askNationality(sc);
        Location location = askLocation(sc); // Поле может быть null, обрабатывается отдельно

        return new Person(persName, passportId, eyeColor, nationality, location);
    }

    private Color askEyeColor(Scanner sc) {
        System.out.println("Выберите цвет глаз (или оставьте пустым):\n    1 - GREEN\n    2 - RED\n    3 - BLUE\n    4 - ORANGE\n    5 - BROWN");
        while (true) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) return null;
            try {
                Color color = Color.getColorByValue(Integer.parseInt(line));
                if (color != null) return color;
            } catch (Exception e) { /* Игнорируем */ }
            System.out.println("Неверный ввод. Пожалуйста, повторите:");
        }
    }

    private Country askNationality(Scanner sc) {
        return askForValidInput(sc,
                "Выберите национальность:\n    1 - GERMANY\n    2 - THAILAND\n    3 - JAPAN",
                s -> Country.getCountryByValue(Integer.parseInt(s)),
                country -> country != null
        );
    }

    private Location askLocation(Scanner sc) {
        System.out.println("Хотите добавить локацию? (y/n)");
        while (true) {
            String answer = sc.nextLine().trim().toLowerCase();
            if ("y".equals(answer)) {
                Float locX = askForValidInput(sc, "Введите X локации (число):", Float::parseFloat, x -> true); // Любое число валидно
                Double locY = askForValidInput(sc, "Введите Y локации (число):", Double::parseDouble, y -> true); // Любое число валидно
                System.out.println("Введите название локации (может быть пустым):");
                String locName = sc.nextLine().trim();
                return new Location(locX, locY, locName);
            } else if ("n".equals(answer)) {
                return null;
            } else {
                System.out.println("Неверный ответ. Введите 'y' (да) или 'n' (нет).");
            }
        }
    }
}