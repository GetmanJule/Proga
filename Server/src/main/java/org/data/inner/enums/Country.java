package org.data.inner.enums;

public enum Country {
    GERMANY,
    THAILAND,
    JAPAN;

    @Override
    public String toString() {
        return name().toUpperCase();
    }

    public static Country getCountry(final String name) {
        return Country.valueOf(name.toUpperCase());
    }

    public static Country getCountryByValue(final int id) {
        switch (id){
            case 1: return GERMANY;
            case 2: return THAILAND;
            case 3: return JAPAN;
            default: return null;
        }
    }
}