package com.thilenius.flame.utilities.types;

/**
 * Created by Alec on 10/24/14.
 */
public class Location2D {

    public int X;
    public int Y;

    public Location2D() {

    }

    public Location2D(int x, int y) {
        X = x;
        Y = y;
    }

    public static Location2D fromString(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        string = string.replace(" ", "");
        String[] tokens = string.split(",");

        if (tokens.length != 2)
            return null;

        try {
            Location2D loc = new Location2D();
            loc.X = Integer.parseInt(tokens[0].trim());
            loc.Y = Integer.parseInt(tokens[1].trim());
            return loc;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override public String toString() {
        return X + ", " + Y;
    }

}
