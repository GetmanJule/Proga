package org.common;

import org.classes.Movie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Объект ответа, отправляемый сервером клиенту.
 * Структурно похож на Kotlin-пример.
 * Содержит текстовый ответ и список дескрипторов команд.
 */
public class Response implements Serializable {
    private static final long serialVersionUID = 1L; // Для совместимости при сериализации

    private String responseText; // Текстовый ответ от сервера
    private List<CommandDescriptor> commandDescriptors; // Список команд, обновляемый сервером
    private List<Movie> movies; // Коллекция фильмов, если команда требует ее (например, 'show')
    private boolean success; // Флаг успешности выполнения команды

    // Конструктор без аргументов для десериализации
    public Response() {
        this.responseText = "Успешно"; // Значение по умолчанию
        this.commandDescriptors = new ArrayList<>(); // Инициализируем изменяемым списком
        this.movies = new ArrayList<>(); // Инициализируем изменяемым списком
        this.success = true;
    }

    public Response(String responseText, boolean success) {
        this(); // Вызываем конструктор по умолчанию для инициализации списков
        this.responseText = responseText;
        this.success = success;
    }

    // Статические фабричные методы для удобного создания ответов
    public static Response success(String message) {
        return new Response(message, true);
    }

    public static Response error(String message) {
        return new Response(message, false);
    }

    // Геттеры
    public String getResponseText() {
        return responseText;
    }

    public List<CommandDescriptor> getCommandDescriptors() {
        return commandDescriptors;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public boolean isSuccess() {
        return success;
    }

    // Сеттеры
    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public void setCommandDescriptors(List<CommandDescriptor> commandDescriptors) {
        this.commandDescriptors = commandDescriptors;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    // Методы для изменения списка команд (аналогичные вашему Kotlin-примеру)
    public void addCommandDescriptors(List<CommandDescriptor> descriptors) {
        if (this.commandDescriptors == null) {
            this.commandDescriptors = new ArrayList<>();
        }
        this.commandDescriptors.addAll(descriptors);
    }

    public void clearCommandDescriptors() {
        if (this.commandDescriptors != null) {
            this.commandDescriptors.clear();
        }
    }

    // Вспомогательный метод для добавления фильмов в ответ
    public void addAllMovies(List<Movie> moviesToAdd) {
        if (this.movies == null) {
            this.movies = new ArrayList<>();
        }
        this.movies.addAll(moviesToAdd);
    }

    @Override
    public String toString() {
        return "Response{" +
                "responseText='" + responseText + '\'' +
                ", commandDescriptorsCount=" + (commandDescriptors != null ? commandDescriptors.size() : 0) +
                ", moviesCount=" + (movies != null ? movies.size() : 0) +
                ", success=" + success +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return success == response.success &&
                Objects.equals(responseText, response.responseText) &&
                Objects.equals(commandDescriptors, response.commandDescriptors) &&
                Objects.equals(movies, response.movies);
    }
    @Override
    public int hashCode() {
        return Objects.hash(responseText, commandDescriptors, movies, success);
    }
}