package org.data;

import org.data.inner.Movie;

import java.io.Serializable;

public class RequestDto implements Serializable {
    private Movie movie;
    private String command;
    private static final long serialVersionUID = 1L;

    public RequestDto(Movie movie, String command) {
        this.movie = movie;
        this.command = command;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
