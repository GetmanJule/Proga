package org.enums;

import java.io.Serializable;

public enum Color implements Serializable {
    GREEN,
    RED,
    BLUE,
    ORANGE,
    BROWN;

    @Override
    public String toString() {
        return super.toString();
    }

    public static Color getColor(final String name) {
        return Color.valueOf(name.toUpperCase());
    }

    public static Color getColorByValue(final int value) {
        switch (value) {
            case 1: return GREEN;
            case 2: return RED;
            case 3: return BLUE;
            case 4: return ORANGE;
            case 5: return BROWN;
            default: return null;
        }
    }
}