/**
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.Expr;

public class PEntityList<D> extends PEntityCollection<D> implements PList<D>{
    public PEntityList(Class<D> type, String entityName, PathMetadata<?> metadata) {
        super(type, entityName, metadata);
    }
    public PEntityList(Class<D> type, String entityName, String var){
        super(type, entityName, PathMetadata.forVariable(var));
    }        
    public PEntity<D> get(Expr<Integer> index) {
        return new PEntity<D>(type, entityName, PathMetadata.forListAccess(this,index));
    }
    public PEntity<D> get(int index) {
        // cache
        return new PEntity<D>(type, entityName, PathMetadata.forListAccess(this,index));
    }
    
}