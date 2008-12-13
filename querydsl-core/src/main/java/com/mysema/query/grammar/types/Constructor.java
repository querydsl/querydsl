package com.mysema.query.grammar.types;

public class Constructor<D> extends Expr<D> {
    private final Expr<?>[] args;
    public Constructor(Class<D> type, Expr<?>... args) {
        super(type);
        this.args = args;
    }
    public Expr<?>[] getArgs() {
        return args;
    }
}