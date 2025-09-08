package org.commands;

import org.classes.Movie;
import org.main.Main;
import org.managerData.MovieCollectionManager;

import java.util.ArrayList;

/*

 */
public class SaveCommand implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        try {
            MovieCollectionManager.writeToFile(Main.filePath);
            mySet = MovieCollectionManager.getData();
            System.out.println("data is saved!");
        } catch (Exception e) {
            System.out.println("Error saving data!");
            /*System.out.println(e);*/
        }

        return true;
    }

    @Override
    public String des() {
        return "save : сохранить коллекцию в файл";
    }

    @Override
    public String getName() {
        return "save";
    }
}
