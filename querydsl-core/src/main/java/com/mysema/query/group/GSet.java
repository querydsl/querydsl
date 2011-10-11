/**
 * 
 */
package com.mysema.query.group;

import java.util.LinkedHashSet;
import java.util.Set;

import com.mysema.query.types.Expression;

class GSet<T> extends AbstractGroupDefinition<T, Set<T>>{
    
    public GSet(Expression<T> expr) {
        super(expr);
    }

    @Override
    public GroupCollector<T,Set<T>> createGroupCollector() {
        return new GroupCollector<T,Set<T>>() {

            private final Set<T> set = new LinkedHashSet<T>();
            
            @Override
            public void add(T o) {
                set.add(o);
            }

            @Override
            public Set<T> get() {
                return set;
            }
            
        };
    }
}