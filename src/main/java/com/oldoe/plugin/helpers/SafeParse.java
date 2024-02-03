package com.oldoe.plugin.helpers;

public class SafeParse {

    public static int tryParseInt(String value, int defaultVal) {

        // Make this method null safe
        if (value == null) {
            return defaultVal;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
