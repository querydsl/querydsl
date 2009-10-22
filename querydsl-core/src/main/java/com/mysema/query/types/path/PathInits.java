/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tiwe
 *
 */
public class PathInits {

    public static final PathInits DEFAULT = new PathInits();
    
    private final Map<String,PathInits> propertyToInits = new HashMap<String,PathInits>();
    
    private boolean initAllProps = false;
    
    public PathInits(String... inits){
        for (String init : inits){
            addInit(init);
        }
    }
    
    private void addInit(String initStr){
        if (initStr.equals("*")){
            initAllProps = true;
        }else{
            String key; 
            String[] inits;
            if (initStr.contains(".")){
                key = initStr.substring(0, initStr.indexOf('.'));
                inits = new String[]{initStr.substring(key.length()+1)};
            }else{
                key = initStr;
                inits = new String[0];
            }
            PathInits init = propertyToInits.get(key);
            if (init == null){
                propertyToInits.put(key, new PathInits(inits));
            }else if (inits.length > 0){
                init.addInit(inits[0]);
            }
        }                  
    }
    
    public PathInits getInits(String property){
        if (propertyToInits.containsKey(property)){
            return propertyToInits.get(property);    
        }else{
            return initAllProps ? DEFAULT : null;
        }
    }

    public boolean isInitialized(String property) {
        return initAllProps || propertyToInits.containsKey(property);
    }
}
