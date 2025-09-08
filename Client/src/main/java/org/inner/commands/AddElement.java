package org.inner.commands;

import org.data.inner.Coordinates;
import org.data.inner.Location;
import org.data.inner.Movie;
import org.data.inner.Person;
import org.data.inner.enums.Color;
import org.data.inner.enums.Country;
import org.data.inner.enums.MpaaRating;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Команда для добавления нового элемента Movie
 */
public class AddElement implements Command {


    @Override
    public Movie doo() {
        // === режим интерактивного ввода ===
        Scanner sc = new Scanner(System.in);

        // название фильма
        String name = "";
        System.out.println("Enter Movie name:");
        while (name.isEmpty()) {
            name = sc.nextLine();
            if (name.isEmpty()) {
                System.out.println("Enter Valid Movie name (Not Empty name):");
            }
        }

        // координата X
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

        // координата Y
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
                corY = null;
            }
        }

        // количество Оскаров
        Long oscar = 0L;
        while (0 >= oscar) {
            System.out.println("Enter oscarsCount(>0):");
            String x = sc.nextLine();
            try {
                oscar = Long.parseLong(x);
            } catch (Exception e) {
                System.out.println("Enter oscarsCount! Please enter Valid Value:");
            }
        }

        // бюджет
        Float budget = 0F;
        while (0 >= budget) {
            System.out.println("Enter budget (>0):");
            String x = sc.nextLine();
            try {
                budget = Float.parseFloat(x);
            } catch (Exception e) {
                System.out.println("Enter budget! Please enter Valid Value:");
                budget = 0F;
            }
        }

        // касса США
        Double usaBoxOffice = 0D;
        while (usaBoxOffice <= 0) {
            System.out.println("Enter usaBoxOffice (>0):");
            String x = sc.nextLine();
            try {
                usaBoxOffice = Double.parseDouble(x);
            } catch (Exception e) {
                System.out.println("Enter usaBoxOffice! Please enter Valid Value:");
                usaBoxOffice = 0D;
            }
        }

        // рейтинг MPAA
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
                mpaaRating = null;
            }
        }

        // оператор фильма
        System.out.println("Person Adding:");
        String persName = "";
        while (persName.isEmpty()) {
            System.out.println("Enter Person Name:");
            persName = sc.nextLine();
        }

        String passportId = "";
        while (passportId.isEmpty()) {
            System.out.println("Enter Person passportId:");
            passportId = sc.nextLine();


        }

        // цвет глаз
        Color perColorEye = null;
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
                perColorEye = null;
                flag = true;
                break;
            }
            try {
                if (Integer.parseInt(x) >= 1 && Integer.parseInt(x) <= 5) {
                    perColorEye = Color.getColorByValue(Integer.parseInt(x));
                    flag = true;
                }
            } catch (Exception e) {
                System.out.println("Enter Person eyeColor! Please enter Valid Value:");
            }
        }

        // национальность
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
                nationality = null;
            }
        }

        // добавление Location
        boolean flagLoc = false;
        while (!flagLoc) {
            System.out.println("Do you want add Location? (y/n) or 1/0");
            String x = sc.nextLine();
            if (!x.isEmpty() && (x.equals("y") || x.equals("1"))) {
                flagLoc = true;
                break;
            }
            if (!x.isEmpty() && (x.equals("n") || x.equals("0"))) {
                break;
            }
        }

        Location loc = null;
        if (flagLoc) {
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

            String locName = "";
            System.out.println("Enter location name:");
            locName = sc.nextLine();

            loc = new Location(locX, locY, locName);
        }

        Movie movie = new Movie(
                name,
                new Coordinates(corX, corY),
                oscar,
                budget,
                usaBoxOffice,
                mpaaRating,
                new Person(persName, passportId, perColorEye, nationality, loc)
        );
        return movie;
    }
}
