package org.inner.commands;

import org.data.inner.Movie;
import org.main.ServerMain;
import org.inner.utils.XMLManager;

import java.util.ArrayList;

/*

 */
public class SaveCommand {
    public String doo() {
        try {
            XMLManager.writeToFile(ServerMain.filePath);
            return "data is saved!" + "\n";
        } catch (Exception e) {
            return "Error saving data!";
        }
    }
}
