package org.inner.commands;

import org.data.inner.Movie;
import org.inner.utils.XMLManager;

import java.util.ArrayList;

/*

 */
public class ClearCommand implements Command {
    @Override
    public String doo(ArrayList<Movie> mySet, String s) {
        XMLManager.dropAll();  // очищаем данные и файл
        mySet.clear();         // синхронизируем локальную коллекцию
        new SaveCommand().doo();
        return "data is dropped!";
    }


    @Override
    public String des() {
        return "clear : очистить коллекцию";
    }

    @Override
    public String getName() {
        return "clear";
    }
}
