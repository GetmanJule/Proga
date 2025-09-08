package org.interactive.commands;

import org.data.Movie;

import java.util.ArrayList;

/*

 */
public interface Command {//базовый интерфейс для команд
    /*

     */
    Movie doo();
    /*

     */
    String des(); //описание
    /*

     */
    String getName();
}