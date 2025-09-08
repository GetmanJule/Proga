package org.data;

import org.data.inner.Movie;

import java.io.Serializable;

public class AnswerDto implements Serializable {

    private Movie movie;
    private String answer;
    private static final long serialVersionUID = 1L;

    public AnswerDto(Movie movie, String answer) {
        this.movie = movie;
        this.answer = answer;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
