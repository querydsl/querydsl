/**
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.ESimple;
import com.mysema.query.types.expr.Expr;

public class PComponentList<D> extends PComponentCollection<D> implements PList<D>{        
    public PComponentList(Class<D> type, PathMetadata<?> metadata) {
        super(type, metadata);
    }
    public PComponentList(Class<D> type, String var){
        super(type, PathMetadata.forVariable(var));
    }        
    public ESimple<D> get(Expr<Integer> index) {
        return new PSimple<D>(type, PathMetadata.forListAccess(this, index));
    }
    public ESimple<D> get(int index) {
        return new PSimple<D>(type, PathMetadata.forListAccess(this, index));
    }        
}