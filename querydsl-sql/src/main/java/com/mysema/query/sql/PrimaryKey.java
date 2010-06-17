package com.mysema.query.sql;

import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PEntity;

/**
 * @author tiwe
 *
 * @param <E>
 * @param <P>
 */
public class PrimaryKey <E,P> implements Key<E,P>{
    
    private final PEntity<E> entity;
    
    private final Path<P> property;
    
    public PrimaryKey(PEntity<E> entity, Path<P> property) {
        this.entity = entity;
        this.property = property;
    }
    
    public PEntity<E> getEntity(){
        return entity;
    }
    
    public Expr<P> getProperty(){
        return property.asExpr();
    }
    
}
