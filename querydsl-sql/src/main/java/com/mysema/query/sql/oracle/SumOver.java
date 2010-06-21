/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.oracle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.types.Expr;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.custom.CNumber;
import com.mysema.query.types.expr.ENumber;

/**
 * SumOver is a fluent type for Oracle specific sum over / partition by / order
 * by constructs
 *
 * @author tiwe
 * @version $Id$
 */
public class SumOver<A extends Number & Comparable<? super A>> extends ENumber<A> {

    private static final long serialVersionUID = -4130672293308756779L;

    // TODO : change this to List<OrderSpecifier<?>>
    private List<Expr<?>> orderBy = new ArrayList<Expr<?>>();

    @Nullable
    private Expr<?> partitionBy;

    private final Expr<A> target;

    public SumOver(Expr<A> expr) {
        super(expr.getType());
        target = expr;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void accept(Visitor v) {
        List<Expr<?>> args = new ArrayList<Expr<?>>();
        StringBuilder builder = new StringBuilder();
        builder.append("sum({0}) over (");
        args.add(target);
        if (partitionBy != null){
            builder.append("partition by {1}");
            args.add(partitionBy);
        }
        if (!orderBy.isEmpty()){
            if (partitionBy != null){
                builder.append(" ");
            }
            builder.append("order by ");
            boolean first = true;
            for (Expr<?> expr : orderBy){
                if (!first){
                    builder.append(", ");
                }
                builder.append("{" + args.size()+"}");
                args.add(expr);
                first = false;
            }
        }
        builder.append(")");
        ENumber<A> expr = CNumber.<A>create(
                (Class<A>)target.getType(),
                builder.toString(),
                args.toArray(new Expr[args.size()]));
        expr.accept(v);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof SumOver){
            SumOver so = (SumOver)o;
            return so.target.equals(target)
                && so.partitionBy.equals(partitionBy)
                && so.orderBy.equals(orderBy);
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return target.hashCode();
    }

    public SumOver<A> order(Expr<?>... orderBy) {
        this.orderBy.addAll(Arrays.asList(orderBy));
        return this;
    }

    public SumOver<A> partition(Expr<?> partitionBy) {
        this.partitionBy = partitionBy;
        return this;
    }

}
