/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import static com.mysema.query.grammar.types.PathMetadata.PROPERTY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryMetadata;
import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.*;
import com.mysema.query.grammar.types.Expr.EConstant;
import com.mysema.query.grammar.types.HqlTypes.DistinctPath;
import com.mysema.query.grammar.types.Quant.QBoolean;
import com.mysema.query.grammar.types.Quant.QComparable;
import com.mysema.query.grammar.types.Quant.QNumber;
import com.mysema.query.grammar.types.Quant.QSimple;
import com.mysema.query.serialization.BaseSerializer;


/**
 * HqlSerializer serializes querydsl expressions into HQL syntax.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HqlSerializer extends BaseSerializer<HqlSerializer>{
    
    private boolean wrapElements = false;
        
    public HqlSerializer(HqlOps ops){
        super(ops);
    }
           
    public void serialize(QueryMetadata<HqlJoinMeta> metadata, boolean forCountRow){
        List<? extends Expr<?>> select = metadata.getProjection();
        List<JoinExpression<HqlJoinMeta>> joins = metadata.getJoins();
        Expr.EBoolean where = metadata.getWhere();
        List<? extends Expr<?>> groupBy = metadata.getGroupBy();
        Expr.EBoolean having = metadata.getHaving();
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();
        
         if (forCountRow){
            append("select count(*)\n");
        }else if (!select.isEmpty()){
            if (!metadata.isDistinct()){
                append("select ");
            }else{
                append("select distinct ");    
            }            
            append(", ", select).append("\n");
        }
        append("from ");
        for (int i=0; i < joins.size(); i++){
            JoinExpression<HqlJoinMeta> je = joins.get(i);            
            if (i > 0){
                String sep = ", ";
                    switch(je.getType()){
                    case FULLJOIN:  sep = "\n  full join "; break;
                    case INNERJOIN: sep = "\n  inner join "; break;
                    case JOIN:      sep = "\n  join "; break;
                    case LEFTJOIN:  sep = "\n  left join "; break;                                
                    }    
                append(sep);
            }
            if (je.getMetadata() != null){
                switch(je.getMetadata()){
                case FETCH: if (!forCountRow) append("fetch "); break;
                }
            }
            
            // type specifier
            if (je.getTarget() instanceof Path.PEntity){
                Path.PEntity<?> pe = (Path.PEntity<?>)je.getTarget();
                if (pe.getMetadata().getParent() == null){
                    String pn = pe.getType().getPackage().getName();
                    String typeName = pe.getType().getName().substring(pn.length()+1); 
                    append(typeName).append(" ");    
                }                
            }            
            handle(je.getTarget());
            if (je.getCondition() != null){
                append(" with ").handle(je.getCondition());
            }
        }
        
        if (where != null){            
            append("\nwhere ").handle(where);
        }
        if (!groupBy.isEmpty()){
            append("\ngroup by ").append(", ",groupBy);
        }
        if (having != null){
            if (groupBy.isEmpty()) {
                throw new IllegalArgumentException("having, but not groupBy was given");
            }                
            append("\nhaving ").handle(having);
        }
        if (!orderBy.isEmpty() && !forCountRow){
            append("\norder by ");
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy){            
                if (!first) builder.append(", ");
                handle(os.getTarget());
                append(os.getOrder() == Order.ASC ? " asc" : " desc");
                first = false;
            }
        }
    }
    
    @Override
    protected void visit(Alias.ASimple<?> expr) {
        handle(expr.getFrom()).append(" as ").append(expr.getTo());        
    }
    
    @Override
    protected void visit(Alias.AToPath expr) {
        handle(expr.getFrom()).append(" as ").visit(expr.getTo());
    }
    
    protected void visit(CountExpression expr) {
        if (expr.getTarget() == null){
            append("count(*)");    
        }else{
            append("count(");
            boolean old = wrapElements;
            wrapElements = true;
            handle(expr.getTarget());
            wrapElements = old;
            append(")");
        }                
    }
    
    protected void visit(DistinctPath<?> expr){
        append("distinct ").visit(expr.getPath());
    }
    
    @Override
    protected void visit(Expr.EConstant<?> expr) {
        boolean wrap = expr.getConstant().getClass().isArray();
        if (wrap) append("(");
        append(":a");
        if (!constants.contains(expr.getConstant())){
            constants.add(expr.getConstant());
            append(Integer.toString(constants.size()));
        }else{
            append(Integer.toString(constants.indexOf(expr.getConstant())+1));
        }        
        if (wrap) append(")");
    }
        
    @Override
    protected void visit(Path.PCollection<?> expr){
        // only wrap a PathCollection, if it the pathType is PROPERTY
        boolean wrap = wrapElements && expr.getMetadata().getPathType().equals(PROPERTY);
        if (wrap) append("elements(");
        visit((Path<?>)expr);
        if (wrap) append(")");
    }
    
    protected void visit(QBoolean<?> q){
        visit((Quant)q);
    }

    protected void visit(QComparable<?> q){
        visit((Quant)q);
    }
    
    protected void visit(QNumber<?> q){
        visit((Quant)q);
    }
    
    protected void visit(QSimple<?> q){
        visit((Quant)q);
    }

    protected void visit(Quant q){        
        visitOperation(null, q.getOperator(), Collections.<Expr<?>>singletonList(q.getTarget()));
    }

    protected void visit(SubQuery<HqlJoinMeta,?> query) {
        append("(");
        serialize(query.getQuery().getMetadata(), false);
        append(")");
    }
    
    private void visitCast(Op<?> operator, Expr<?> source, Class<?> targetType) {
        append("cast(").handle(source);
        append(" as ");
        append(targetType.getSimpleName().toLowerCase()).append(")");
        
    }

    protected void visitOperation(Class<?> type, Op<?> operator, List<Expr<?>> args) {    
        boolean old = wrapElements;
        wrapElements = HqlOps.wrapCollectionsForOp.contains(operator);    
        // 
        if (operator.equals(Ops.ISTYPEOF)){
            args = new ArrayList<Expr<?>>(args);
            args.set(1, new EConstant<String>( ((Class<?>)((EConstant<?>)args.get(1)).getConstant()).getName()));
            super.visitOperation(type, operator, args);
        }else if (operator.equals(Ops.STRING_CAST)){
            visitCast(operator, args.get(0), String.class);
        }else if (operator.equals(Ops.NUMCAST)){
            visitCast(operator, args.get(0), (Class<?>) ((EConstant<?>)args.get(1)).getConstant());
        }else{
            super.visitOperation(type, operator, args);    
        }        
        //
        wrapElements = old;
    }
    
    

}
