package org.inner.commands;

import org.data.inner.Movie;
import org.main.ServerMain;
import org.inner.utils.XMLManager;

import java.util.ArrayList;

/*

 */
public class SaveCommand implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        try {
            XMLManager.writeToFile(ServerMain.filePath);
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
