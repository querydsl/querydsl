package com.mysema.query.types.alias;

import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;

/**
 * Entity as alias
 */
public class AEntity<D> extends EEntity<D> implements AToPath{
    private final Expr<?> from;
    private final Path<?> to;
    public AEntity(PEntity<D> from, PEntity<D> to) {
        super(from.getType());
        this.from = from;
        this.to = to;
    }
    public Expr<?> getFrom() {return from;}
    public  Path<?> getTo() {return to;}  
}