package com.mysema.query.sql;

import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

/**
 * @author tiwe
 *
 * @param <E>
 * @param <P>
 */
public class ForeignKey <E,P> implements Key<E,P>{
    
    private final PEntity<?> entity;
    
    private final Path<P> property;
    
    public ForeignKey(PEntity<?> entity, Path<P> property) {
        this.entity = entity;
        this.property = property;
    }
    
    public PEntity<?> getEntity(){
        return entity;
    }
    
    public Expr<P> getProperty(){
        return property.asExpr();
    }
    
    public EBoolean eq(Key<E,P> key){
        return property.asExpr().eq(key.getProperty());
    }
    
}
