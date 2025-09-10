package org.inner.commands;

import org.data.inner.Coordinates;
import org.data.inner.Location;
import org.data.inner.Movie;
import org.data.inner.Person;
import org.data.inner.enums.Color;
import org.data.inner.enums.Country;
import org.data.inner.enums.MpaaRating;

import java.util.Scanner;

public class UpdateCommand implements Command {

    private long id; // id объекта для обновления

    public boolean parseCommand(String input) {
        // Проверка, что введено update {id}
        if (input == null || !input.matches("^update\\s+\\d+$")) {
            return false;
        }
        this.id = Long.parseLong(input.split("\\s+")[1]);
        return true;
    }

    public long getId() {
        return id;
    }

    @Override
    public Movie doo() {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Обновление объекта Movie ===");

        // === Movie Name ===
        String name = "";
        while (name.isEmpty()) {
            System.out.print("Enter Movie name: ");
            name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Имя не может быть пустым!");
            }
        }

        // === Coordinates ===
        Float corX = null;
        while (corX == null) {
            System.out.print("Enter corX (max 906): ");
            try {
                corX = Float.parseFloat(sc.nextLine());
                if (corX > 906) corX = null;
            } catch (Exception ignored) {}
        }

        Long corY = null;
        while (corY == null) {
            System.out.print("Enter corY (max 655): ");
            try {
                corY = Long.parseLong(sc.nextLine());
                if (corY > 655) corY = null;
            } catch (Exception ignored) {}
        }

        // === OscarsCount ===
        Long oscar = 0L;
        while (oscar <= 0) {
            System.out.print("Enter oscarsCount (>0): ");
            try {
                oscar = Long.parseLong(sc.nextLine());
            } catch (Exception ignored) {}
        }

        // === Budget ===
        Float budget = 0F;
        while (budget <= 0) {
            System.out.print("Enter budget (>0): ");
            try {
                budget = Float.parseFloat(sc.nextLine());
            } catch (Exception ignored) {}
        }

        // === USA Box Office ===
        Double usaBoxOffice = 0D;
        while (usaBoxOffice <= 0) {
            System.out.print("Enter usaBoxOffice (>0): ");
            try {
                usaBoxOffice = Double.parseDouble(sc.nextLine());
            } catch (Exception ignored) {}
        }

        // === MpaaRating ===
        MpaaRating mpaaRating = null;
        while (mpaaRating == null) {
            System.out.print("Enter mpaaRating (1-G,2-PG,3-PG_13,4-R,5-NC_17): ");
            try {
                mpaaRating = MpaaRating.getRating(Integer.parseInt(sc.nextLine()));
            } catch (Exception ignored) {}
        }

        // === Person ===
        System.out.print("Enter Person Name: ");
        String persName = sc.nextLine().trim();

        System.out.print("Enter Person passportId: ");
        String passportId = sc.nextLine().trim();

        Color perColorEye = null;
        while (true) {
            System.out.print("Enter eyeColor (1-GREEN,2-RED,3-BLUE,4-ORANGE,5-BROWN, Enter - skip): ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                // пользователь не ввёл цвет глаз → оставляем null
                break;
            }
            try {
                perColorEye = Color.getColorByValue(Integer.parseInt(input));
                break;
            } catch (Exception ignored) {
                System.out.println("Некорректный ввод! Введите число от 1 до 5 или Enter для пропуска.");
            }
        }

        Country nationality = null;
        while (nationality == null) {
            System.out.print("Enter nationality (1-GERMANY,2-THAILAND,3-JAPAN): ");
            try {
                nationality = Country.getCountryByValue(Integer.parseInt(sc.nextLine()));
            } catch (Exception ignored) {}
        }

        // === Location ===
        Location loc = null;
        System.out.print("Add Location? (y/n): ");
        String ans = sc.nextLine().trim();
        if (ans.equalsIgnoreCase("y") || ans.equals("1")) {
            loc = new Location();
            System.out.print("Enter location X: ");
            loc.setX(Float.parseFloat(sc.nextLine()));
            System.out.print("Enter location Y: ");
            loc.setY(Double.parseDouble(sc.nextLine()));
            System.out.print("Enter location name: ");
            loc.setName(sc.nextLine());
        }

        // === Создаём Movie объект ===
        Movie movie = new Movie();
        movie.setName(name);
        movie.setCoordinates(new Coordinates(corX, corY));
        movie.setOscarsCount(oscar);
        movie.setBudget(budget);
        movie.setUsaBoxOffice(usaBoxOffice);
        movie.setMpaaRating(mpaaRating);
        movie.setOperator(new Person(persName, passportId, perColorEye, nationality, loc));

        return movie;
    }
}
