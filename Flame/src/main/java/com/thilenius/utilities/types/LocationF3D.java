package com.thilenius.utilities.types;

/**
 * Created by Alec on 11/17/14.
 */
public class LocationF3D {
    public float X;
    public float Y;
    public float Z;

    public LocationF3D() {

    }

    public LocationF3D(float x, float y, float z) {
        X = x;
        Y = y;
        Z = z;
    }

    public LocationF3D(String commaDelimString) {

        if (commaDelimString == null || commaDelimString == "")
            return;

        String[] tokens = commaDelimString.split(",");

        if (tokens.length != 3)
            return;

        X = Float.parseFloat(tokens[0].trim());
        Y = Float.parseFloat(tokens[1].trim());
        Z = Float.parseFloat(tokens[2].trim());
    }

    public Location3D toLocation3D() {
        return new Location3D(Math.round(X), Math.round(Y), Math.round(Z));
    }

    public LocationF3D scale (float f) {
        return new LocationF3D(X * f, Y * f, Z * f);
    }

    @Override public String toString() {
        return X + ", " + Y + ", " + Z;
    }
}
