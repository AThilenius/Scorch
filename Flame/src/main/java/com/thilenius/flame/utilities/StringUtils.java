package com.thilenius.flame.utilities;

/**
 * Created by Alec on 1/27/15.
 */
public class StringUtils {

    public static String Shrink (String fullString, String secondaryString, String truncateString, int maxSize) {
        if (fullString.length() <= maxSize) {
            return fullString;
        }

        if (secondaryString.length() <= maxSize) {
            return secondaryString;
        }

        if (truncateString.length() <= maxSize) {
            return truncateString;
        }

        return truncateString.substring(0, maxSize - 1);
    }

    public static boolean nullOrEmpty (String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }

        return false;
    }

}
