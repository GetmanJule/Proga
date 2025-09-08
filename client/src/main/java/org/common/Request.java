package org.common;

import org.classes.Movie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Объект запроса, отправляемый клиентом серверу.
 * Структурно похож на Kotlin-пример.
 * Содержит команду с аргументами, объект Movie и учетные данные пользователя.
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L; // Для совместимости при сериализации

    private List<String> body; // Тело запроса: команда и ее аргументы
    private Movie movie;       // Объект Movie, если команда его требует
    private String username;   // Логин пользователя (для ЛР7)
    private String password;   // Пароль пользователя (для ЛР7)

    // Конструктор без аргументов для десериализации
    public Request() {
        this.body = new ArrayList<>();
    }

    public Request(List<String> body, Movie movie, String username, String password) {
        this.body = body != null ? body : new ArrayList<>();
        this.movie = movie;
        this.username = username;
        this.password = password;
    }

    // Упрощенные конструкторы для удобства
    public Request(List<String> body, Movie movie) {
        this(body, movie, null, null);
    }

    public Request(List<String> body) {
        this(body, null, null, null);
    }

    // Геттеры
    public List<String> getBody() {
        return body;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Сеттеры
    public void setBody(List<String> body) {
        this.body = body;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Request{" +
                "body=" + body +
                (movie != null ? ", movie=" + movie.getName() : "") +
                (username != null ? ", username='" + username + '\'' : "") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(body, request.body) &&
                Objects.equals(movie, request.movie) &&
                Objects.equals(username, request.username) &&
                Objects.equals(password, request.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, movie, username, password);
    }
}