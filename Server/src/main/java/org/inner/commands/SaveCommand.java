package org.inner.commands;

import org.data.inner.Movie;
import org.main.ServerMain;
import org.inner.utils.XMLManager;

import java.util.ArrayList;

/*

 */
public class SaveCommand {
    public boolean doo(ArrayList<Movie> mySet) {
        try {
            XMLManager.writeToFile(ServerMain.filePath);
            mySet = XMLManager.getData();
            System.out.println("data is saved!");
        } catch (Exception e) {
            System.out.println("Error saving data!");
            /*System.out.println(e);*/
        }

        return true;
    }
    public boolean doo() {
        try {
            XMLManager.writeToFile(ServerMain.filePath);
            System.out.println("data is saved!");
        } catch (Exception e) {
            System.out.println("Error saving data!");
            /*System.out.println(e);*/
        }

        return true;
    }
}
