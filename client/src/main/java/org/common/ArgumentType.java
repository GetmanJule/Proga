package org.common;

import java.io.Serializable;

/**
 * Типы аргументов, которые могут принимать команды.
 * Для передачи метаданных о командах между клиентом и сервером.
 */
public enum ArgumentType implements Serializable {
    STRING,    // Строковый аргумент
    INTEGER,   // Целочисленный аргумент (для int)
    LONG,      // Длинное целочисленный аргумент (для long)
    FLOAT,     // Число с плавающей точкой одинарной точности (для float)
    DOUBLE,    // Число с плавающей точкой двойной точности (для double)
    ENUM_MPAA, // Для MpaaRating
    ENUM_COLOR, // Для Color
    ENUM_COUNTRY, // Для Country
    NO_ARGS;   // Команда не принимает никаких строковых аргументов

    private static final long serialVersionUID = 1L; // Для совместимости при сериализации
}