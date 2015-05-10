package com.thilenius.flame.utilities.types;

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

    @Override
    public boolean equals(Object other) {
        if (other instanceof Location3D) {
            Location3D otherLoc = (Location3D) other;
            return X == otherLoc.X && Y == otherLoc.Y && Z == otherLoc.Z;
        }

        return false;
    }

    public static Location3D fromString(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        string = string.replace(" ", "");
        String[] tokens = string.split(",");

        if (tokens.length != 3)
            return null;

        try {
            Location3D loc = new Location3D();
            loc.X = Integer.parseInt(tokens[0].trim());
            loc.Y = Integer.parseInt(tokens[1].trim());
            loc.Z = Integer.parseInt(tokens[2].trim());
            return loc;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override public String toString() {
        return X + ", " + Y + ", " + Z;
    }

}
