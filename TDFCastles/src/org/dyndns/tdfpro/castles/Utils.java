package org.dyndns.tdfpro.castles;

public class Utils {

    public static float clamp(float c, float min, float max) {
        return c < min ? min : c > max ? max : c;
    }

}
