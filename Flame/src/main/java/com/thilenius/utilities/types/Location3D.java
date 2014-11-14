package com.thilenius.utilities.types;

/**
 * Created by Alec on 10/24/14.
 */
public class Location3D {

    public int X;
    public int Y;
    public int Z;

    public Location3D() {

    }

    public Location3D(int x, int y, int z) {
        X = x;
        Y = y;
        Z = z;
    }

    public Location3D(String commaDelimString) {

        if (commaDelimString == null || commaDelimString == "")
            return;

        String[] tokens = commaDelimString.split(",");

        if (tokens.length != 3)
            return;

        X = Integer.parseInt(tokens[0].trim());
        Y = Integer.parseInt(tokens[1].trim());
        Z = Integer.parseInt(tokens[2].trim());
    }

    @Override public String toString() {
        return X + ", " + Y + ", " + Z;
    }

}
