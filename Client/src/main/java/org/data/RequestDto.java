package org.data;

import org.data.inner.Movie;

import java.io.Serializable;

public class RequestDto implements Serializable {
    private String command;
    private Movie movie;
    private String login;
    private String password;
    private static final long serialVersionUID = 1L;

    public RequestDto(Movie movie, String command, String login, String password) {
        this.movie = movie;
        this.command = command;
        this.login = login;
        this.password = password;
    }

    public RequestDto(){}

    public String getCommand() {
        return command;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}

