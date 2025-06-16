package org.commands;

import org.classes.Movie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

/*

 */
public class RemoveGreatherElement implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        String[] idS = s.split(" ");
        long id = -111111;
        try {
            id = Long.parseLong(idS[1]);
        }catch (Exception e){
            System.out.println("Id should be a number!");
            return false;
        }
        ArrayList<Movie> newMySet = new ArrayList<>();

        for(int i = 0; i < mySet.size(); i++){
            if(mySet.get(i).getId() >= id){
                System.out.println("Object with id " + mySet.get(i).getId() + " deleted!");
            }else {
                newMySet.add(mySet.get(i));
            }
        }

        mySet.clear();
        mySet.addAll(newMySet);
        mySet.sort(Comparator.comparing(Movie::getNameUpperCase));
        return false;
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
