/**
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.CollectionType;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;

public interface PCollection<D> extends Path<java.util.Collection<D>>, CollectionType<D>{        
    Class<D> getElementType();
    EBoolean contains(D child);
    EBoolean contains(Expr<D> child);
    EComparable<Integer> size();
}