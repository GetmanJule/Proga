package org.inner.commands;

import org.data.Movie;

import java.util.ArrayList;

/*

 */
public class FilterStartsWithName implements Command{
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        if(s.split(" ").length == 1){
            System.out.println("Please enter the name of the movie");
            return true;
        }

        if (mySet.size() != 0){
            for (Movie movie : mySet){
                if (movie.getName().startsWith(s.split(" ")[1])){
                    System.out.println(movie.getName() + " start with " + s);
                }
            }
        }else{
            System.out.println("No movies found!");
        }

        return true;
    }

    @Override
    public String des() {
        return "filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки";
    }

    @Override
    public String getName() {
        return "filter_starts_with_name";
    }
}
