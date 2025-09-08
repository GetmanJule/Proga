package org.inner.commands;

import org.data.Movie;

import java.util.ArrayList;
import java.util.Comparator;

/*

 */
public class RemoveFirsrtCommand implements Command{
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        if(!mySet.isEmpty()){
            for(Movie m : mySet){
                    mySet.remove(m);
                    System.out.println("Removed " + m.getName() + " from the data");
                mySet.sort(Comparator.comparing(Movie::getNameUpperCase));
                    return true;
            }
        }else{
            System.out.println("No such movie!");
        }

        return true;
    }

    @Override
    public String des() {
        return "remove_first : удалить первый элемент из коллекции";
    }

    @Override
    public String getName() {
        return "remove_first";
    }
}
