package com.thilenius.flame.utilities;

/**
 * Created by Alec on 3/30/15.
 */
public class MathUtils {

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

}
