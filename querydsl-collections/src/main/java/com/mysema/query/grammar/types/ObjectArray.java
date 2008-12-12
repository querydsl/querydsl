package com.mysema.query.grammar.types;

import com.mysema.query.grammar.types.Expr;

/**
 * ObjectArray represents an Object array expression
 * 
 * @author tiwe
 * @version $Id$
 */
public class ObjectArray extends Expr<Object[]> {
    private final Expr<?>[] args;
    public ObjectArray(Expr<?>... args) {
        super(null);
        this.args = args;
    }
    public Expr<?>[] getArgs() {
        return args;
    }

}
