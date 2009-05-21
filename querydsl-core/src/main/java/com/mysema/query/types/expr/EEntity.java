/**
 * 
 */
package com.mysema.query.types.expr;


public abstract class EEntity<D> extends Expr<D>{
    public EEntity(Class<? extends D> type) {super(type);}        
}