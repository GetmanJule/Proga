package org.inner.commands;

import org.data.Coordinates;
import org.data.Location;
import org.data.Movie;
import org.data.Person;
import org.data.enums.Color;
import org.data.enums.Country;
import org.data.enums.MpaaRating;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/*

 */
public class UpdateCommand implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        if (s.split(" ").length != 2) {
            return "Usage: update <id>";
        }

        long id;
        try {
            id = Long.parseLong(s.split(" ")[1]);
        } catch (NumberFormatException e) {
            return "Id should be a number!";
        }

        // Проверяем, есть ли фильм с таким id
        Movie movieToUpdate = null;
        for (Movie m : mySet) {
            if (m.getId() == id) {
                movieToUpdate = m;
                break;
            }
        }

        if (movieToUpdate == null) {
            return "Object with id " + id + " not found!";
        }

        Scanner sc = new Scanner(System.in);

        // === Начало обновления ===
        String name = "";
        System.out.println("Enter Movie name:");
        while (name.isEmpty()) {
            name = sc.nextLine();
            if (name.isEmpty()) {
                System.out.println("Enter Valid Movie name (Not Empty name):");
            }
        }

        Float corX = null;
        System.out.println("Enter corX (max=906) :");
        while (corX == null) {
            String x = sc.nextLine();
            try {
                corX = Float.parseFloat(x);
                if (corX > 906) {
                    System.out.println("Enter corX (max=906)! Please enter Valid Range:");
                    corX = null;
                }
            } catch (Exception e) {
                System.out.println("Enter corX (max=906)! Please enter Valid Value:");
            }
        }

        Long corY = null;
        System.out.println("Enter corY (max=655) :");
        while (corY == null) {
            String x = sc.nextLine();
            try {
                corY = Long.parseLong(x);
                if (corY > 655) {
                    System.out.println("Enter corY (max=655)! Please enter Valid Range:");
                    corY = null;
                }
            } catch (Exception e) {
                System.out.println("Enter corY (max=655)! Please enter Valid Value:");
            }
        }

        Long oscar = 0L;
        while (oscar <= 0) {
            System.out.println("Enter oscarsCount(>0):");
            String x = sc.nextLine();
            try {
                oscar = Long.parseLong(x);
            } catch (Exception e) {
                System.out.println("Enter oscarsCount! Please enter Valid Value:");
            }
        }

        Float budget = 0F;
        while (budget <= 0) {
            System.out.println("Enter budget (>0):");
            String x = sc.nextLine();
            try {
                budget = Float.parseFloat(x);
            } catch (Exception e) {
                System.out.println("Enter budget! Please enter Valid Value:");
            }
        }

        Double usaBoxOffice = 0D;
        while (usaBoxOffice <= 0) {
            System.out.println("Enter usaBoxOffice (>0):");
            String x = sc.nextLine();
            try {
                usaBoxOffice = Double.parseDouble(x);
            } catch (Exception e) {
                System.out.println("Enter usaBoxOffice! Please enter Valid Value:");
            }
        }

        MpaaRating mpaaRating = null;
        while (mpaaRating == null) {
            System.out.println("Enter mpaaRating: \n" +
                               "    1 - G\n" +
                               "    2 - PG\n" +
                               "    3 - PG_13\n" +
                               "    4 - R\n" +
                               "    5 - NC_17");
            String x = sc.nextLine();
            try {
                mpaaRating = MpaaRating.getRating(Integer.parseInt(x));
            } catch (Exception e) {
                System.out.println("Enter mpaaRating! Please enter Valid Value:");
            }
        }

        // === Person ===
        String persName = "";
        while (persName.isEmpty()) {
            System.out.println("Enter Person Name:");
            persName = sc.nextLine();
        }

        String passportId = "";
        while (passportId.isEmpty()) {
            System.out.println("Enter Person passportId:");
            passportId = sc.nextLine();

            for (Movie m : mySet) {
                if (m.getOperator().getPassportID().equals(passportId)) {
                    System.out.println("Person passportID must be unique:");
                    passportId = "";
                }
            }
        }

        Color perClorEye = null;
        boolean flag = false;
        System.out.println("Enter Person eyeColor: \n" +
                           "    1 - GREEN\n" +
                           "    2 - RED\n" +
                           "    3 - BLUE\n" +
                           "    4 - ORANGE\n" +
                           "    5 - BROWN");
        while (!flag) {
            String x = sc.nextLine();

            if (x.isEmpty()) {
                perClorEye = null;
                flag = true;
                break;
            }
            try {
                int val = Integer.parseInt(x);
                if (val >= 1 && val <= 5) {
                    perClorEye = Color.getColorByValue(val);
                    flag = true;
                }
            } catch (Exception e) {
                System.out.println("Enter Person eyeColor! Please enter Valid Value:");
            }
        }

        Country nationality = null;
        while (nationality == null) {
            System.out.println("Enter Person nationality: \n" +
                               "    1 - GERMANY\n" +
                               "    2 - THAILAND\n" +
                               "    3 - JAPAN");
            String x = sc.nextLine();
            try {
                nationality = Country.getCountryByValue(Integer.parseInt(x));
            } catch (Exception e) {
                System.out.println("Enter Person nationality! Please enter Valid Value:");
            }
        }

        // === Location (optional) ===
        Location loc = null;
        System.out.println("Do you want add Location? (y/n) or 1/0");
        String ans = sc.nextLine();
        if (ans.equals("y") || ans.equals("1")) {
            loc = new Location();

            Float locX = null;
            System.out.println("Enter location X:");
            while (locX == null) {
                String x = sc.nextLine();
                try {
                    locX = Float.parseFloat(x);
                } catch (Exception e) {
                    System.out.println("Enter location X! Please enter Valid Value:");
                }
            }

            Double locY = null;
            System.out.println("Enter location Y:");
            while (locY == null) {
                String y = sc.nextLine();
                try {
                    locY = Double.parseDouble(y);
                } catch (Exception e) {
                    System.out.println("Enter location Y! Please enter Valid Value:");
                }
            }

            System.out.println("Enter location name:");
            String locName = sc.nextLine();

            loc.setX(locX);
            loc.setY(locY);
            loc.setName(locName);
        }

        // === Обновляем объект ===
        movieToUpdate.setName(name);
        movieToUpdate.setCoordinates(new Coordinates(corX, corY));
        movieToUpdate.setOscarsCount(oscar);
        movieToUpdate.setUsaBoxOffice(usaBoxOffice);
        movieToUpdate.setMpaaRating(mpaaRating);
        movieToUpdate.setOperator(new Person(persName, passportId, perClorEye, nationality, loc));

        mySet.sort(Comparator.comparing(Movie::getNameUpperCase));

        return "Object with id " + id + " successfully updated!";
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
