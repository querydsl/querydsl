/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.mssql;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.custom.CNumber;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PNumber;

/**
 * RowNumber supports row_number constructs for MS SQL Server
 * 
 * @author tiwe
 *
 */
public class RowNumber extends Expr<Long>{

    private static final long serialVersionUID = 3499501725767772281L;

    private final List<Expr<?>> partitionBy = new ArrayList<Expr<?>>();
    
    private final List<OrderSpecifier<?>> orderBy = new ArrayList<OrderSpecifier<?>>();
    
    @Nullable
    private PNumber<Long> target;
    
    public RowNumber() {
        super(Long.class);
    }
    
    @Override
    public void accept(Visitor v) {
        List<Expr<?>> args = new ArrayList<Expr<?>>(partitionBy.size() + orderBy.size());
        StringBuilder builder = new StringBuilder("row_number() over (");
        if (!partitionBy.isEmpty()){
            builder.append("partition by ");
            appendPartition(args, builder);
        }
        if (!orderBy.isEmpty()){
            if (!partitionBy.isEmpty()){
                builder.append(" ");
            }            
            builder.append("order by ");
            appendOrder(args, builder);
        }        
        builder.append(")");
        
        if (target != null){
            builder.append(" as {" + args.size() + "}");
            args.add(target);
        }
        
        ENumber<Long> expr = CNumber.create(Long.class, builder.toString(), 
                args.toArray(new Expr[args.size()]));
        expr.accept(v);
    }

    // TODO : externalize
    private void appendPartition(List<Expr<?>> args, StringBuilder builder) {
        boolean first = true;
        for (Expr<?> expr : partitionBy){
            if (!first){
                builder.append(", ");
            }
            builder.append("{"+args.size()+"}");
            args.add(expr);
            first = false;
        }
    }

    // TODO : externalize
    private void appendOrder(List<Expr<?>> args, StringBuilder builder) {
        boolean first = true;
        for (OrderSpecifier<?> expr : orderBy){
            if (!first){
                builder.append(", ");
            }
            builder.append("{" + args.size()+"}");            
            if (!expr.isAscending()){
                builder.append(" desc");
            }
            args.add(expr.getTarget());
            first = false;
        }
    }
    
    public RowNumber orderBy(OrderSpecifier<?>... order){
        for (OrderSpecifier<?> o : order){
            orderBy.add(o);
        }
        return this;
    }
    
    public RowNumber orderBy(EComparable<?>... order){
        for (EComparable<?> o : order){
            orderBy.add(o.asc());
        }
        return this;
    }

    public RowNumber partitionBy(Expr<?>... exprs){
        for (Expr<?> expr : exprs){
            partitionBy.add(expr);
        }
        return this;
    }
    
    public RowNumber as(PNumber<Long> target){
        this.target = target;
        return this;
    }
    
    @Override
    public int hashCode(){
        return orderBy.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof RowNumber){
            RowNumber rn = (RowNumber)o;
            return partitionBy.equals(rn.partitionBy) && orderBy.equals(rn.orderBy);
        }else{
            return false;
        }
    }

}
