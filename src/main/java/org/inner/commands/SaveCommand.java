package org.inner.commands;

import org.data.Movie;
import org.main.Main;
import org.inner.utils.XMLManager;

import java.util.ArrayList;

/*

 */
public class SaveCommand implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        try {
            XMLManager.writeToFile(Main.filePath);
            mySet = XMLManager.getData();
            return "data is saved!" + "\n";
        } catch (Exception e) {
            return "Error saving data!";
        }
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
