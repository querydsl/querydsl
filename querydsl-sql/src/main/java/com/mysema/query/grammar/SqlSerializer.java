/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryBase;
import com.mysema.query.grammar.types.Constructor;
import com.mysema.query.grammar.types.CountExpression;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.SubQuery;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.serialization.BaseSerializer;

/**
 * SqlSerializer serializes Querydsl queries into SQL
 *
 * @author tiwe
 * @version $Id$
 */
public class SqlSerializer extends BaseSerializer<SqlSerializer>{
    
    private SqlOps ops;
    
    public SqlSerializer(SqlOps ops){
        super(ops);
        this.ops = ops;
    }
    
    public void serialize(List<Expr<?>> select, List<JoinExpression<SqlJoinMeta>> joins,
        Expr.EBoolean where, List<Expr<?>> groupBy, Expr.EBoolean having,
        List<OrderSpecifier<?>> orderBy, int limit, int offset, boolean forCountRow){
         if (forCountRow){
//            _append("select count(*)\n");
             _append(ops.select())._append(ops.countStar());
        }else if (!select.isEmpty()){
            _append(ops.select());           
            List<Expr<?>> sqlSelect = new ArrayList<Expr<?>>();
            for (Expr<?> selectExpr : select){
                if (selectExpr instanceof Constructor){
                    // transforms constructor arguments into individual select
                    // expressions
                    sqlSelect.addAll(Arrays.<Expr<?>>asList(((Constructor<?>)selectExpr).getArgs()));
                }else{
                    sqlSelect.add(selectExpr);
                }
            }
            _append(", ", sqlSelect);
        }
        _append(ops.from());
        if (joins.isEmpty()){
            // TODO : disallow usage of dummy table ?!?
            _append(ops.dummyTable());
            
        }        
        for (int i=0; i < joins.size(); i++){
            JoinExpression<SqlJoinMeta> je = joins.get(i);            
            if (i > 0){
                String sep = ", ";
                    switch(je.getType()){
                    case FULLJOIN:  sep = ops.fullJoin(); break;
                    case INNERJOIN: sep = ops.innerJoin(); break;
                    case JOIN:      sep = ops.join(); break;
                    case LEFTJOIN:  sep = ops.leftJoin(); break;                                
                    }    
                _append(sep);
            }
            
            // type specifier
            if (je.getTarget() instanceof Path.PEntity && ops.supportsAlias()){
                Path.PEntity<?> pe = (Path.PEntity<?>)je.getTarget();
                if (pe.getMetadata().getParent() == null){ 
                    _append(pe.getEntityName())._append(ops.aliasAs());    
                }                
            }            
            handle(je.getTarget());
            if (je.getCondition() != null){
                _append(ops.on()).handle(je.getCondition());
            }
        }
        
        if (where != null){            
            _append(ops.where()).handle(where);
        }
        if (!groupBy.isEmpty()){
            _append(ops.groupBy())._append(", ",groupBy);
        }
        if (having != null){
            if (groupBy.isEmpty()) {
                throw new IllegalArgumentException("having, but not groupBy was given");
            }                
            _append(ops.having()).handle(having);
        }
        if (!orderBy.isEmpty() && !forCountRow){
            _append(ops.orderBy());
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy){            
                if (!first) builder.append(", ");
                handle(os.target);
                _append(os.order == Order.ASC ? ops.asc() : ops.desc());
                first = false;
            }
        }
        if (ops.limitAndOffsetSymbols()){
            if (limit > 0){
                _append(ops.limit())._append(String.valueOf(limit));
            }
            if (offset > 0){
                _append(ops.offset())._append(String.valueOf(offset));
            }    
        }               
    }
    
    public void serializeUnion(List<Expr<?>> select,
            SubQuery<SqlJoinMeta, ?>[] sqs, EBoolean self,
            List<OrderSpecifier<?>> orderBy) {
        // union
        _append(ops.union(), Arrays.asList(sqs));
        
        
        // order by
        if (!orderBy.isEmpty()){
            _append(ops.orderBy());
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy){            
                if (!first) builder.append(", ");
                handle(os.target);
                _append(os.order == Order.ASC ? ops.asc() : ops.desc());
                first = false;
            }
        }
        
    }
    
    protected void visit(CountExpression expr) {
        if (expr.getTarget() == null){
            _append(ops.countStar());    
        }else{
            _append(ops.count())._append("(").handle(expr.getTarget())._append(")");
        }                
    }
               
    @Override
    protected void visit(Expr.EConstant<?> expr) {
        _append("?");
        constants.add(expr.getConstant());
    }

    protected void visit(SubQuery<SqlJoinMeta,?> query) {
        QueryBase<SqlJoinMeta,?>.Metadata md = query.getQuery().getMetadata();
        _append("(");
        serialize(md.getSelect(), md.getJoins(),
            md.getWhere(), md.getGroupBy(), md.getHaving(), 
            md.getOrderBy(), 0, 0, false);
        _append(")");
    }

}
