package org.inner.commands;

import org.data.Movie;
import org.main.Main;
import org.inner.utils.XMLManager;

import java.util.ArrayList;

/*

 */
public class SaveCommand implements Command {
    @Override
    public boolean doo(ArrayList<Movie> mySet, String s) {
        try {
            XMLManager.writeToFile(Main.filePath);
            mySet = XMLManager.getData();
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
