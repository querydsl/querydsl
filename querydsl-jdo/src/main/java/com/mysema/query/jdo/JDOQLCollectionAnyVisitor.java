package com.mysema.query.jdo;

import com.mysema.query.support.CollectionAnyVisitor;
import com.mysema.query.types.Predicate;

/**
 * @author tiwe
 *
 */
public final class JDOQLCollectionAnyVisitor extends CollectionAnyVisitor{
    
    public static final JDOQLCollectionAnyVisitor DEFAULT = new JDOQLCollectionAnyVisitor();
    
    @Override
    protected Predicate exists(Context c, Predicate condition){
        return condition;
    }
    
    private JDOQLCollectionAnyVisitor(){}

}
