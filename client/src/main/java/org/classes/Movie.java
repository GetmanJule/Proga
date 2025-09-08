package org.classes;

import org.enums.MpaaRating;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * XMLManager for managing XML DB
 */
public class Movie implements Comparable<Movie>, Serializable {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long oscarsCount; //Значение поля должно быть больше 0, Поле не может быть null
    private float budget; //Значение поля должно быть больше 0
    private double usaBoxOffice; //Значение поля должно быть больше 0
    private MpaaRating mpaaRating; //Поле не может быть null
    private Person operator; //Поле не может быть null

    public Movie(String name, Coordinates coordinates, Long oscarsCount, float budget, double usaBoxOffice, MpaaRating mpaaRating, Person operator) {
        this.id = 0;//заглушка, чтобы изменить на сервере
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();//автоматически, на сервере замениться
        this.oscarsCount = oscarsCount;
        this.budget = budget;
        this.usaBoxOffice = usaBoxOffice;
        this.mpaaRating = mpaaRating;
        this.operator = operator;
    }

    public Movie() {
    }

    public Movie(long id, String name, Coordinates coordinates, LocalDateTime creationDate, Long oscarsCount, float budget, double usaBoxOffice, MpaaRating mpaaRating, Person operator) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.oscarsCount = oscarsCount;
        this.budget = budget;
        this.usaBoxOffice = usaBoxOffice;
        this.mpaaRating = mpaaRating;
        this.operator = operator;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public String getNameUpperCase() {
        return name.toUpperCase();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getOscarsCount() {
        return oscarsCount;
    }

    public void setOscarsCount(Long oscarsCount) {
        this.oscarsCount = oscarsCount;
    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public double getUsaBoxOffice() {
        return usaBoxOffice;
    }

    public void setUsaBoxOffice(double usaBoxOffice) {
        this.usaBoxOffice = usaBoxOffice;
    }

    public MpaaRating getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(MpaaRating mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public Person getOperator() {
        return operator;
    }

    public void setOperator(Person operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "id: " + this.id
                + "\nname: " + this.name
                + "\ncoordinates: " + this.coordinates
                + "\ncreationDate: " + this.creationDate
                + "\noscarsCount: " + this.oscarsCount
                + "\nbudget: " + this.budget
                + "\nusaBoxOffice: " + this.usaBoxOffice
                + "\nmpaaRating: " + this.mpaaRating
                + "\noperator: " + this.operator;
    }

    @Override
    public int compareTo(Movie otherMovie) {
        // БЕЗОПАСНЫЙ СПОСОБ СРАВНЕНИЯ long. Предотвращает ошибку переполнения int.
        return Long.compare(this.id, otherMovie.id);
    }
}