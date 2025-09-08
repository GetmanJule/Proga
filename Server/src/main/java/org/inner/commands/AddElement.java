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

/**
 * Команда для добавления нового элемента Movie
 */
public class AddElement implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        // === режим добавления из скрипта (одной строкой) ===
        if (s.split(" ").length != 1) {
            String[] all = s.split(" ");
            try {
                for (Movie m : mySet) {
                    if (m.getOperator().getPassportID().equals(all[9])) {
                        return "Ошибка: Person passportID '" + all[9] + "' уже существует!";
                    }
                }

                Movie movie = new Movie(
                        all[1],
                        new Coordinates(Float.parseFloat(all[2]), Long.parseLong(all[3])),
                        Long.parseLong(all[4]),
                        Float.parseFloat(all[5]),
                        Double.parseDouble(all[6]),
                        MpaaRating.getRating(Integer.parseInt(all[7])),
                        new Person(
                                all[8],
                                all[9],
                                Color.getColorByValue(Integer.parseInt(all[10])),
                                Country.getCountryByValue(Integer.parseInt(all[11])),
                                new Location(
                                        Float.parseFloat(all[12]),
                                        Double.parseDouble(all[13]),
                                        all[14]
                                )
                        )
                );
                mySet.add(movie);
                mySet.sort(Comparator.comparing(Movie::getNameUpperCase));
                return "Фильм '" + all[1] + "' успешно добавлен из скрипта!";
            } catch (Exception e) {
                e.printStackTrace();
                return "Ошибка при добавлении фильма из скрипта!";
            }
        }

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

            for (Movie m : mySet) {
                if (m.getOperator().getPassportID().equals(passportId)) {
                    System.out.println("Person passportID must be unique:");
                    passportId = "";
                }
            }
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

        mySet.add(movie);
        mySet.sort(Comparator.comparing(Movie::getNameUpperCase));

        return "Фильм '" + name + "' успешно добавлен через интерактивный ввод!";
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
