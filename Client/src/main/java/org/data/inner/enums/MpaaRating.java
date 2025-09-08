package org.data.inner.enums;

import java.io.Serializable;

public enum MpaaRating implements Serializable {
    G,
    PG,
    PG_13,
    R,
    NC_17;

    @Override
    public String toString() {
        return super.toString();
    }

    public static MpaaRating getRating(final String name) {
        return MpaaRating.valueOf(name.toUpperCase());
    }

    public static MpaaRating getRating(final int ordinal) {
        switch (ordinal) {
            case 1: return G;
            case 2: return PG;
            case 3: return PG_13;
            case 4: return R;
            case 5: return NC_17;
            default: return null;

        }
    }
}