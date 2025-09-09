package org.data.inner;

import java.io.Serializable;

/**
 * XMLManager for managing XML DB
 */
public class Coordinates implements Serializable {
    private float x; //Максимальное значение поля: 906
    private Long y; //Максимальное значение поля: 655, Поле не может быть null
    private static final long serialVersionUID = 1L;

    public Coordinates() {

    }

    public Coordinates(float x, Long y) {
        this.x = x;
        this.y = y;
    }


    public float getX() {
        return x;
    }

    /**
     * @param x this is x
     *
     */
    public void setX(float x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" + "x=" + x + ", y=" + y + '}';
    }
}
