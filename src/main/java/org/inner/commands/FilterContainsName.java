package org.inner.commands;

import org.data.Movie;

import java.util.ArrayList;

/*

 */
public class FilterContainsName implements Command{
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        if(s.split(" ").length == 1){
            System.out.println("Please enter the name of the movie");
            return true;
        }

        if (mySet.size() != 0){
            for (Movie movie : mySet){//перебор во всех фильмах
                if (movie.getName().contains(s.split(" ")[1])){
                    System.out.println(movie.getName() + " contains " + s);
                }
            }
        }else{
            System.out.println("No movies found!");
        }

        return true;
    }

    @Override
    public String des() {
        return "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку";
    }

    @Override
    public String getName() {
        return "filter_contains_name";
    }
}
