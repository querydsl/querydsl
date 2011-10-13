package com.mysema.query.jpa;

import com.mysema.query.support.CollectionAnyVisitor;
import com.mysema.query.support.Context;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.PredicateOperation;

/**
 * @author tiwe
 *
 */
public final class JPQLCollectionAnyVisitor extends CollectionAnyVisitor{
    
    public static final JPQLCollectionAnyVisitor DEFAULT = new JPQLCollectionAnyVisitor();
    
    @Override
    protected Predicate exists(Context c, Predicate condition) {
        JPQLSubQuery query = new JPQLSubQuery();
        for (int i = 0; i < c.paths.size(); i++) {
            query.from(c.replacements.get(i));
            query.where(new PredicateOperation(Ops.IN, c.replacements.get(i), c.paths.get(i).getMetadata().getParent()));    
        }        
        c.clear();
        query.where(condition);
        return query.exists();
    }
    
    private JPQLCollectionAnyVisitor() {}

}
