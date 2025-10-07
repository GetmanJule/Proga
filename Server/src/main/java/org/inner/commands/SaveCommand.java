package org.inner.commands;

import org.data.inner.Movie;
import org.main.ServerMain;
import org.inner.utils.XMLManager;

import java.util.ArrayList;
import java.util.List;

/*

 */
public class SaveCommand {
    public boolean doo(List<Movie> mySet) {
        try {
            XMLManager.writeToFile("data/save.xml");
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
            XMLManager.writeToFile("data/save.xml");
            System.out.println("data is saved!");
        } catch (Exception e) {
            System.out.println("Error saving data!");
            /*System.out.println(e);*/
        }

        return true;
    }
}
