/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Expr.EString;

/**
 * ExprFactory provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface ExprFactory {
    
    EBoolean createBoolean(Op<Boolean> operator, Expr<?>... args);
    
    <OpType, RT extends Comparable<? super RT>> EComparable<RT> createComparable(Class<RT> type, Op<OpType> operator, Expr<?>... args);
    
    <A> Expr<A> createConstant(A obj);

    <OpType extends Number,D extends Number & Comparable<? super D>> ENumber<D> createNumber(Class<? extends D> type, Op<OpType> operator, Expr<?>... args);
    
    EString createString(Op<String> operator, Expr<?>... args);
    
    Expr<String[]> createStringArray(Op<String> operator, Expr<?>... args);

}
