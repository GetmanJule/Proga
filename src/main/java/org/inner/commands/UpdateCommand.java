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
    public boolean doo(ArrayList<Movie> mySet, String s) {
        if(s.split(" ").length == 2){
            long id = Long.parseLong(s.split(" ")[1]);
            boolean found = false;
            for(Movie m : mySet){
                if(m.getId() == id){
                    found = true;
                }
            }
            if(!found){
                System.out.println("Object with id " + id + " not found!");
                return false;
            }
            System.out.println("Starting to update object with id " + id);

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

            for(Movie m : mySet){
                if(m.getId() == id){
                    m.setName(name);
                    m.setCoordinates(new Coordinates(corX, corY));
                    m.setOscarsCount(oscar);
                    m.setUsaBoxOffice(usaBoxOffice);
                    m.setMpaaRating(mpaaRating);
                    m.setOperator(new Person(persName, passportId, perClorEye, nationality, loc));
                }
            }
            System.out.println("Object with id " + id + " updated!");

            mySet.sort(Comparator.comparing(Movie::getNameUpperCase));
            return true;


        }

        return true;
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
