/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.util;

import java.beans.Introspector;

import org.apache.commons.lang3.StringUtils;

/**
 * @author tiwe
 *
 */
public final class BeanUtils {

    public static String capitalize(String name) {
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1))) {
            return name;
        } else {
            return StringUtils.capitalize(name);
        }
    }

    public static String uncapitalize(String name) {
        return Introspector.decapitalize(name);
    }

    private BeanUtils(){}
}
