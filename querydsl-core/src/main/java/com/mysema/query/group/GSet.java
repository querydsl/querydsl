/**
 * 
 */
package com.mysema.query.group;

import java.util.LinkedHashSet;
import java.util.Set;

import com.mysema.query.types.Expression;

class GSet<T> extends AbstractGroupExpression<T, Set<T>>{
    
    private static final long serialVersionUID = -1575808026237160843L;

    public GSet(Expression<T> expr) {
        super(Set.class, expr);
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