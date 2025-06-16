package org.commands;


import org.classes.Movie;

import java.util.ArrayList;
import java.util.TreeSet;

/*

 */
public class ShowCommand implements Command{
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        if(mySet.size() > 0){
            for(Movie m : mySet){
                System.out.println(m);
            }
        }else {
            System.out.println("data is empty!");
        }

        return true;
    }

    @Override
    public String des() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }

    @Override
    public String getName() {
        return "show";
    }
}
