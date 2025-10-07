package org.inner.commands;

import org.data.inner.Movie;

import java.util.List;

/*

 */
public interface Command {//базовый интерфейс для команд

    /*

     */
    String doo(List<Movie> mySet, String s);

    /*

     */
    String des(); //описание

    /*

     */
    String getName();

    default void setArg(Movie movie) {
        //
    }

    default void setLogin(String login){

    }

}