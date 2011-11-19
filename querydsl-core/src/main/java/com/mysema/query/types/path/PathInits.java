/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * PathInits defines path initializations that can be attached to
 * properties via QueryInit annotations
 *
 * @author tiwe
 *
 */
public class PathInits implements Serializable{

    private static final long serialVersionUID = -2173980858324141095L;

    public static final PathInits DEFAULT = new PathInits();

    public static final PathInits DIRECT  = new PathInits("*");

    private boolean initAllProps = false;

    private final Map<String,PathInits> propertyToInits = new HashMap<String,PathInits>();
    
    private PathInits defaultValue = DEFAULT;

    public PathInits(String... inits) {
        for (String init : inits) {
            addInit(init);
        }
    }

    private void addInit(String initStr) {
        if (initStr.equals("*")) {
            initAllProps = true;
        } else if (initStr.startsWith("*.")) {
            initAllProps = true;
            defaultValue = new PathInits(initStr.substring(2));
        } else {
            String key;
            String[] inits;
            if (initStr.contains(".")) {
                key = initStr.substring(0, initStr.indexOf('.'));
                inits = new String[]{initStr.substring(key.length()+1)};
            } else {
                key = initStr;
                inits = new String[0];
            }
            PathInits init = propertyToInits.get(key);
            if (init == null) {
                propertyToInits.put(key, new PathInits(inits));
            }else if (inits.length > 0) {
                init.addInit(inits[0]);
            }
        }
    }

    public PathInits get(String property) {
        if (propertyToInits.containsKey(property)) {
            return propertyToInits.get(property);
        } else if (initAllProps) {
            return defaultValue;
        } else {
            throw new IllegalArgumentException(property + " is not initialized");
        }
    }

    public boolean isInitialized(String property) {
        return initAllProps || propertyToInits.containsKey(property);
    }
}
