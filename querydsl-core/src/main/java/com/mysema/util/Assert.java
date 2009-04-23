/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.util;

/**
 * Assert provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Assert {
        
    /**
     * use notEmpty(String) instead
     */
    @Deprecated
    public static String hasText(String text) {
        return notEmpty(text);
    }

    public static <T> T notNull(T object) {
        return notNull(object, "was null");
    }

    public static <T> T notNull(T object, String message) {
        if (object == null) throw new IllegalArgumentException(message);
        return object;
    }
    
    public static String notEmpty(String text) {
        if (text == null || text.equals("")) throw new IllegalArgumentException("was empty");
        return text;
    }
    
    public static <T> T[] notEmpty(T[] objects) {
        if(objects == null || objects.length == 0) throw new IllegalArgumentException("was empty");
        return objects;
    }

}
