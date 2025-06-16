package org.commands;

import org.classes.Movie;

import java.util.ArrayList;
import java.util.HashSet;
/*

 */
public interface Command {//базовый интерфейс для команд
    /*

     */
    boolean doo(ArrayList<Movie> mySet, String s);
    /*

     */
    String des(); //описание
    /*

     */
    String getName();
}