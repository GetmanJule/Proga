package org.commands;

import org.classes.Coordinates;
import org.classes.Location;
import org.classes.Movie;
import org.classes.Person;
import org.enums.Color;
import org.enums.Country;
import org.enums.MpaaRating;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.SimpleTimeZone;

/**
 * XMLManager for managing XML DB
 */
public class AddElement implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {//добавление нового Movie в mySet, принимает строку
        if(s.split(" ").length != 1){//если писать в одну строку, как execute_script
            /*      0  1 2  3 4  5   6  7      8      9 10 11  12      13*/
            /*add mov1 3 3 67 67 354 1 Maks id4350430 3 3 4354 4565 sdfdsdfgk*/
            String[] all = s.split(" ");

            try {
                for(Movie m: mySet){
                    if(m.getOperator().getPassportID().equals(all[9])){
                        System.out.println("Person passportID must be a unique:");
                        return true;
                    }
                }

                Movie movie = new Movie(all[1], new Coordinates(Float.parseFloat(all[2]), Long.parseLong(all[3])),
                        Long.parseLong(all[4]), Float.parseFloat(all[5]), Double.parseDouble(all[6]), MpaaRating.getRating(Integer.parseInt(all[7])),
                        new Person(all[8], all[9], Color.getColorByValue(Integer.parseInt(all[10])), Country.getCountryByValue(Integer.parseInt(all[11])),
                                new Location(Float.parseFloat(all[12]), Double.parseDouble(all[13]), all[14])));
                mySet.add(movie);
            }catch (Exception e) {
                e.printStackTrace();
                System.out.println("Adding movie failed!");
            }

            mySet.sort(Comparator.comparing(Movie::getNameUpperCase));

            System.out.println("Adding movie from script successfully!");
        }
        




        if(s.split(" ").length == 1){//если по одному числу, значению
            Scanner sc = new Scanner(System.in);

            String name = "";
            System.out.println("Enter Movie name:");
            while (name.isEmpty()){
                name = sc.nextLine();
                if(name.isEmpty()) {
                    System.out.println("Enter Valid Movie name (Not Empty name):");
                }
            }

            Float corX = null;
            System.out.println("Enter corX (max=906) :");
            while (corX == null){
                String x = sc.nextLine();
                try {
                    corX = Float.parseFloat(x);
                    if (corX > 906) {
                        System.out.println("Enter corX (max=906)! Please enter Valid Range:");
                        corX = null;
                    }
                }catch (Exception e) {
                    System.out.println("Enter corX (max=906)! Please enter Valid Value:");
                }

            }

            Long corY = null;
            System.out.println("Enter corY (max=655) :");
            while (corY == null){
                String x = sc.nextLine();
                try {
                    corY = Long.parseLong(x);//преобразует строку в тип Long
                    if (corY > 655){
                        System.out.println("Enter corY (max=655)! Please enter Valid Range:");
                        corY = null;
                    }
                }catch (Exception e){
                    System.out.println("Enter corY (max=655)! Please enter Valid Value:");
                    corY = null;
                }
            }

            Long oscar = 0L;
            while (0 >= oscar) {
                System.out.println("Enter oscarsCount(>0):");
                String x = sc.nextLine();
                try {
                    oscar = Long.parseLong(x);
                }catch (Exception e){
                    System.out.println("Enter oscarsCount! Please enter Valid Value:");
                }
            }

            Float budget = 0F;
            while (0 >= budget){
                System.out.println("Enter budget (>0):");
                String x = sc.nextLine();
                try {
                    budget = Float.parseFloat(x);
                }catch (Exception e){
                    System.out.println("Enter budget! Please enter Valid Value:");
                    budget = 0F;
                }
            }

            Double usaBoxOffice = 0D;
            while (usaBoxOffice <= 0){
                System.out.println("Enter usaBoxOffice (>0):");
                String x = sc.nextLine();
                try {
                    usaBoxOffice = Double.parseDouble(x);
                }catch (Exception e){
                    System.out.println("Enter usaBoxOffice! Please enter Valid Value:");
                    usaBoxOffice = 0D;
                }
            }

            MpaaRating mpaaRating = null;
            while (mpaaRating == null){
                System.out.println("Enter mpaaRating: \n" +
                        "    1 - G\n" +
                        "    2 - PG\n" +
                        "    3 - PG_13\n" +
                        "    4 - R\n" +
                        "    5 - NC_17");
                String x = sc.nextLine();
                try {
                    mpaaRating = MpaaRating.getRating(Integer.parseInt(x));
                }catch (Exception e){
                    System.out.println("Enter mpaaRating! Please enter Valid Value:");
                    mpaaRating = null;
                }
            }

            System.out.println("Person Adding:");

            String persName = "";
            while (persName.isEmpty()){
                System.out.println("Enter Person Name:");
                persName = sc.nextLine();
            }

            String passportId = "";
            while (passportId.isEmpty()){
                System.out.println("Enter Person passportId:");
                passportId = sc.nextLine();

                for(Movie m: mySet){
                    if(m.getOperator().getPassportID().equals(passportId)){
                        System.out.println("Person passportID must be a unique:");
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
            while (!flag){
                String x = sc.nextLine();

                    if(x.isEmpty()){
                        perClorEye = null;
                        flag = true;
                        break;
                    }
                    try {

                        if (Integer.parseInt(x) >= 1 && 6 >= Integer.parseInt(x)) {
                            perClorEye = Color.getColorByValue(Integer.parseInt(x));
                            flag = true;
                            break;
                        }
                    }
                catch (Exception e){
                        System.out.println("Enter Person eyeColor! Plese enter Valid Value:");
                    }

            }

            Country nationality = null;
            while (nationality == null){
                System.out.println("Enter Person nationality: \n" +
                        "    1 - GERMANY\n" +
                        "    2 - THAILAND\n" +
                        "    3 - JAPAN");
                String x = sc.nextLine();
                try {
                    nationality = Country.getCountryByValue(Integer.parseInt(x));
                }catch (Exception e){
                    System.out.println("Enter Person nationality! Plese enter Valid Value:");
                    nationality = null;
                }
            }

            boolean flagLov = false;
            while (!flagLov){
                System.out.println("Do you want add Location? (y/n) or 1/0");
                String x = sc.nextLine();
                try {
                    if(!x.isEmpty() && x.equals("y") || x.equals("1")){
                        flagLov = true;
                    }
                    if(!x.isEmpty() && x.equals("n") || x.equals("0")){
                        break;
                    }
                }catch (Exception e){
                    System.out.println("Please enter valid answer!");
                }
            }

            Location loc = new Location();
            if(flagLov) {
                Float locX = null;
                System.out.println("Enter location X:");
                while (locX == null) {
                    String x = sc.nextLine();
                    try {
                        locX = Float.parseFloat(x);
                    } catch (Exception e) {
                        System.out.println("Enter location X! Plese enter Valid Value:");
                        locX = null;
                    }
                }

                Double locY = null;
                System.out.println("Enter location Y:");
                while (locY == null) {
                    String y = sc.nextLine();
                    try {
                        locY = Double.parseDouble(y);
                    } catch (Exception e) {
                        System.out.println("Enter location Y! Plese enter Valid Value:");
                        locY = null;
                    }
                }

                String locName = null;
                boolean l = false;
                while (!l) {
                    System.out.println("Enter locatin name:");
                    locName = sc.nextLine();
                    if (locName.isEmpty()){
                        locName = "";
                        l = true;
                    }else{
                        l = true;
                    }
                }

                loc.setX(locX);
                loc.setY(locY);
                loc.setName(locName);
            }else{
                loc = null;
            }

            Movie movie = new Movie(name, new Coordinates(corX, corY), oscar, budget, usaBoxOffice,mpaaRating, new Person(persName, passportId, perClorEye, nationality, loc));
            mySet.add(movie);
            mySet.sort(Comparator.comparing(Movie::getNameUpperCase));
        }

        return true;
    }

    @Override
    public String des() {
        return "add {element} : добавить новый элемент в data";
    }

    @Override
    public String getName() {
        return "add";
    }
}
