/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */

package com.mysema.query.support;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;

public class Context {
    
    public boolean replace;
    
    public final List<Path<?>> paths = new ArrayList<Path<?>>(); 
        
    public final List<EntityPath<?>> replacements = new ArrayList<EntityPath<?>>();    
            
    public void add(Path<?> anyPath, EntityPath<?> replacement){
        replace = true;
        paths.add(anyPath);
        replacements.add(replacement);
    }
    
    public void add(Context c){
        replace |= c.replace;
        paths.addAll(c.paths);
        replacements.addAll(c.replacements);

    }

    public void clear() {
        paths.clear();
        replacements.clear();            
    }
    
}