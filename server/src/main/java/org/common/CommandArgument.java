package org.common;

import java.io.Serializable;
import java.util.Objects;

/**
 * Описание одного аргумента команды.
 * Используется в CommandDescriptor для информирования клиента о структуре команды.
 */
public class CommandArgument implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name; // Имя аргумента (для вывода в help)
    private ArgumentType type; // Тип данных аргумента
    private boolean isOptional; // Является ли аргумент необязательным
    private String description; // Дополнительное описание аргумента (может быть null)

    // Конструктор без аргументов для десериализации (обязателен для многих механизмов)
    public CommandArgument() {}

    public CommandArgument(String name, ArgumentType type, boolean isOptional, String description) {
        this.name = name;
        this.type = type;
        this.isOptional = isOptional;
        this.description = description;
    }

    // Геттеры
    public String getName() {
        return name;
    }

    public ArgumentType getType() {
        return type;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public String getDescription() {
        return description;
    }

    // Сеттеры (могут быть использованы десериализатором)
    public void setName(String name) {
        this.name = name;
    }

    public void setType(ArgumentType type) {
        this.type = type;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CommandArgument{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", isOptional=" + isOptional +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandArgument that = (CommandArgument) o;
        return isOptional == that.isOptional &&
                Objects.equals(name, that.name) &&
                type == that.type &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, isOptional, description);
    }
}