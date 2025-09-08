package org.interactive.commands;

import org.data.inner.Movie;

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