/**
 * 
 */
package com.mysema.query.types.custom;

import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;

public abstract class CComparable<T extends Comparable<?>>
        extends EComparable<T> implements Custom<T> {
    public CComparable(Class<T> type) {
        super(type);
    }
    public Expr<?> getArg(int index) {return getArgs().get(index);}
}