/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.util.Map;

/**
 * APUtils provides utilities for APT code generation in Querydsl
 * 
 * @author tiwe
 * @version $Id$
 */
public class APTUtils {

    public static String getString(Map<String, String> options, String key,
            String defaultValue) {
        String prefix = "-A" + key + "=";
        for (Map.Entry<String, String> entry : options.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                return entry.getKey().substring(prefix.length());
            } else if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return defaultValue;
    }

}
