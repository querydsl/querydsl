/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.mssql;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.custom.CNumber;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.Path;

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
        if (!orderBy.isEmpty()){
            if (!partitionBy.isEmpty()){
                builder.append(" ");
            }            
            builder.append("order by ");
            boolean first = true;
            for (OrderSpecifier<?> expr : orderBy){
                if (!first){
                    builder.append(", ");
                }
                builder.append("{" + args.size()+"}");
                args.add(expr.getTarget());
                if (!expr.isAscending()){
                    builder.append(" desc");
                }
                first = false;
            }
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
    
    public RowNumber orderBy(OrderSpecifier<?>... order){
        for (OrderSpecifier<?> o : order){
            orderBy.add(o);
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
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof RowNumber){
            // TODO
            return false;
        }else{
            return false;
        }
    }

}
