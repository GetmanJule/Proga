package org.classes;

import org.enums.Color;
import org.enums.Country;

import java.io.Serializable;

/**
 * XMLManager for managing XML DB
 */
public class Person implements Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String passportID; //Значение этого поля должно быть уникальным, Поле не может быть null
    private Color eyeColor; //Поле может быть null
    private Country nationality; //Поле не может быть null
    private Location location; //Поле может быть null

    public Person(){

    }

    public Person(Person person) {
        // Копирующий конструктор
        this.name = person.name;
        this.passportID = person.passportID;
        this.eyeColor = person.eyeColor;
        this.nationality = person.nationality;
        this.location = person.location;
    }

    public Person(String name, String passportID, Color eyeColor, Country nationality, Location location) {
        this.name = name;
        this.passportID = passportID;
        this.eyeColor = eyeColor;
        this.nationality = nationality;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "PerName: " + this.name
                + "\nPassportID: " + this.passportID
                + "\nEyeColor: " + this.eyeColor
                + "\nNationality: " + this.nationality
                + "\nLocation: " + this.location;
    }
}