/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.util.Map;

/**
 * APUtils provides
 *
 * @author tiwe
 * @version $Id$
 */
public class APTUtils {
    
    public static String getString(Map<String, String> options, String prefix,
            String defaultValue) {
        for (Map.Entry<String, String> entry : options.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                return entry.getKey().substring(prefix.length());
            }
        }
        return defaultValue;
    }

}
