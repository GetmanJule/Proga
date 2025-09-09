package org.inner.commands;

import org.data.inner.Movie;

import java.util.ArrayList;

/*

 */
public interface Command {//базовый интерфейс для команд

    /*

     */
    String doo(ArrayList<Movie> mySet, String s);

    /*

     */
    String des(); //описание

    /*

     */
    String getName();

    default void setArg(Movie movie) {
        //
    }

}