package com.querydsl.core.util;

/**
 * String utilities
 *
 * @author Jan-Willem Gmelig Meyling
 */
public final class StringUtils {

    private StringUtils() {
    }

    public static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String uncapitalize(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

}
