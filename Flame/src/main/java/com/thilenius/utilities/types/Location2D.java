package com.thilenius.utilities.types;

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

    public Location2D(String commaDelimString) {

        if (commaDelimString == null || commaDelimString == "")
            return;

        String[] tokens = commaDelimString.split(",");

        if (tokens.length != 2)
            return;

        X = Integer.parseInt(tokens[0].trim());
        Y = Integer.parseInt(tokens[1].trim());
    }

    @Override public String toString() {
        return X + ", " + Y;
    }

}
