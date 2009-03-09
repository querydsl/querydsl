package com.mysema.query.util;

/**
 * Assert provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Assert {
    
    public static String hasText(String string) {
        if (string == null || string.equals("")) throw new IllegalArgumentException("was empty");
        return string;
    }

    public static <T> T notNull(T object) {
        if (object == null) throw new IllegalArgumentException("was null");
        return object;
    }

}
