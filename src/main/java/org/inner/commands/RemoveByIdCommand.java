package org.inner.commands;

import org.data.Movie;

import java.util.ArrayList;
import java.util.Comparator;

/*

 */
public class RemoveByIdCommand implements Command{
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        if(s.split(" ").length == 1){
            System.out.println("Please enter the ID of the movie");
            return true;
        }

        boolean flag = false;
        if(!mySet.isEmpty()){
            for(Movie m : mySet){
                if(m.getId() == Integer.parseInt(s.split(" ")[1])){
                    mySet.remove(m);
                    System.out.println("Removed " + m.getName() + " from the data");
                    mySet.sort(Comparator.comparing(Movie::getNameUpperCase));
                    flag = true;
                }
            }
        }else{
            System.out.println("No such movie!");
        }

        if (flag == false){
            System.out.println("No such movie!");
        }
        return true;
    }

    @Override
    public String des() {
        return "remove_by_id id : удалить элемент из коллекции по его id";
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }
}
