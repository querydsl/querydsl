/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.util;

import org.apache.commons.lang.StringUtils;

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
    public static String hasText(String text) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("was blank");
        } else {
            return text;
        }
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
