package org.commands;

import org.classes.Movie;

import java.util.ArrayList;




public class PrintUniqueBudget implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        ArrayList<Float> tmp = new ArrayList<>();
        if (mySet.size() != 0){
            for (Movie movie : mySet){
                if (!isInArray(tmp, movie.getBudget())){
                    System.out.println(movie.getId() + " is unique: " + movie.getBudget());
                    tmp.add(movie.getBudget());
                }
            }
        }
        return true;
    }

    public boolean isInArray(ArrayList<Float> mySet, float x) {
        for(int i = 0; i < mySet.size(); i++){
            if (mySet.get(i) == x){
                return true;
            }
        }
        return false;
    }

    @Override
    public String des() {
        return "print_unique_budget : вывести уникальные значения поля budget всех элементов в коллекции";
    }

    @Override
    public String getName() {
        return "print_unique_budget";
    }
}
