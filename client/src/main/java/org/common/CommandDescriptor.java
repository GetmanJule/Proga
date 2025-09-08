package org.common;

import java.io.Serializable;
import java.util.Objects;

/**
 * Описывает доступную команду сервера.
 * Используется для передачи метаданных команд клиенту (например, для команды 'help').
 */
public class CommandDescriptor implements Serializable {
    private static final long serialVersionUID = 1L; // Для совместимости при сериализации

    private String name; // Имя команды (например, "add", "show")
    private String description; // Описание команды
    private String[] arguments; // Ожидаемые аргументы (для валидации на клиенте)
    private boolean requiresMovie; // Требуется ли объект Movie для этой команды (например, для "add")

    // Конструктор без аргументов для десериализации
    public CommandDescriptor() {}

    public CommandDescriptor(String name, String description, String[] arguments, boolean requiresMovie) {
        this.name = name;
        this.description = description;
        this.arguments = arguments;
        this.requiresMovie = requiresMovie;
    }

    // Геттеры
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getArguments() {
        return arguments;
    }

    public boolean requiresMovie() {
        return requiresMovie;
    }

    // Сеттеры (могут быть использованы десериализатором)
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public void setRequiresMovie(boolean requiresMovie) {
        this.requiresMovie = requiresMovie;
    }

    @Override
    public String toString() {
        return name + " " + (arguments.length > 0 ? String.join(" ", arguments) : "") + ": " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandDescriptor that = (CommandDescriptor) o;
        return requiresMovie == that.requiresMovie &&
                Objects.equals(name, that.name) &&
                java.util.Arrays.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, description, requiresMovie);
        result = 31 * result + java.util.Arrays.hashCode(arguments);
        return result;
    }
}