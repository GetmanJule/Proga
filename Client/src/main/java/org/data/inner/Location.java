package org.data.inner;

import java.io.Serializable;

/**
 * XMLManager for managing XML DB
 */
public class Location implements Serializable {
    private Float x;
    private Double y; //Поле не может быть null
    private String name; //Строка не может быть пустой, Поле может быть null
    private static final long serialVersionUID = 1L;

    public Location() {

    }

    public Location(Float x, Double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" + "x=" + x + ", y=" + y + ", name=" + this.name + '}';
    }
}