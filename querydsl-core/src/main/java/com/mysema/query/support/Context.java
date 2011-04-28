/**
 * 
 */
package com.mysema.query.support;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;

public class Context {
    
    public boolean replace;
    
    public final List<Path<?>> anyPaths = new ArrayList<Path<?>>(); 
        
    public final List<EntityPath<?>> replacements = new ArrayList<EntityPath<?>>();    
            
    public void add(Path<?> anyPath, EntityPath<?> replacement){
        replace = true;
        anyPaths.add(anyPath);
        replacements.add(replacement);
    }
    
    public void add(Context c){
        replace |= c.replace;
        anyPaths.addAll(c.anyPaths);
        replacements.addAll(c.replacements);

    }

    public void clear() {
        anyPaths.clear();
        replacements.clear();            
    }
    
}