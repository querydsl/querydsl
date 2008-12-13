/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import static com.mysema.query.grammar.types.PathMetadata.LISTVALUE_CONSTANT;
import static com.mysema.query.grammar.types.PathMetadata.PROPERTY;
import static com.mysema.query.grammar.types.PathMetadata.VARIABLE;

import java.util.List;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryBase;
import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.Alias;
import com.mysema.query.grammar.types.Custom;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.Quant;
import com.mysema.query.grammar.types.HqlTypes.CountExpression;
import com.mysema.query.grammar.types.HqlTypes.DistinctPath;
import com.mysema.query.grammar.types.HqlTypes.SubQuery;
import com.mysema.query.grammar.types.PathMetadata.PathType;
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
           
    public void serialize(List<Expr<?>> select, List<JoinExpression<JoinMeta>> joins,
        Expr.Boolean where, List<Expr<?>> groupBy, Expr.Boolean having,
        List<OrderSpecifier<?>> orderBy, boolean forCountRow){
         if (forCountRow){
            _append("select count(*)\n");
        }else if (!select.isEmpty()){
            _append("select ")._append(", ", select)._append("\n");
        }
        _append("from ");
        for (int i=0; i < joins.size(); i++){
            JoinExpression<JoinMeta> je = joins.get(i);            
            if (i > 0){
                String sep = ", ";
                    switch(je.getType()){
                    case FULLJOIN:  sep = "\n  full join "; break;
                    case INNERJOIN: sep = "\n  inner join "; break;
                    case JOIN:      sep = "\n  join "; break;
                    case LEFTJOIN:  sep = "\n  left join "; break;                                
                    }    
                _append(sep);
            }
            if (je.getMetadata() != null){
                switch(je.getMetadata()){
                case FETCH: if (!forCountRow) _append("fetch "); break;
                }
            }
            
            // type specifier
            if (je.getTarget() instanceof Path.Entity){
                Path.Entity<?> pe = (Path.Entity<?>)je.getTarget();
                if (pe.getMetadata().getParent() == null){
                    String pn = pe.getType().getPackage().getName();
                    String typeName = pe.getType().getName().substring(pn.length()+1); 
                    _append(typeName)._append(" ");    
                }                
            }            
            handle(je.getTarget());
            if (je.getCondition() != null){
                _append(" with ").handle(je.getCondition());
            }
        }
        
        if (where != null){            
            _append("\nwhere ").handle(where);
        }
        if (!groupBy.isEmpty()){
            _append("\ngroup by ")._append(", ",groupBy);
        }
        if (having != null){
            if (groupBy.isEmpty()) {
                throw new IllegalArgumentException("having, but not groupBy was given");
            }                
            _append("\nhaving ").handle(having);
        }
        if (!orderBy.isEmpty() && !forCountRow){
            _append("\norder by ");
            boolean first = true;
            for (OrderSpecifier<?> os : orderBy){            
                if (!first) builder.append(", ");
                handle(os.target);
                _append(os.order == Order.ASC ? " asc" : " desc");
                first = false;
            }
        }
    }
    
    @Override
    protected void visit(Alias.Simple<?> expr) {
        handle(expr.getFrom())._append(" as ")._append(expr.getTo());        
    }
    
    @Override
    protected void visit(Alias.ToPath expr) {
        handle(expr.getFrom())._append(" as ").visit(expr.getTo());
    }
    
    protected void visit(CountExpression expr) {
        if (expr.getTarget() == null){
            _append("count(*)");    
        }else{
            _append("count(").handle(expr.getTarget())._append(")");
        }                
    }
    
    protected void visit(Custom.Boolean expr){
        visit((Custom<?>)expr);
    }
    
    protected void visit(Custom.Comparable<?> expr){
        visit((Custom<?>)expr);
    }
    
    protected void visit(Custom.String expr){
        visit((Custom<?>)expr);
    }
    
    protected void visit(Custom<?> expr){
        Object[] strings = new String[expr.getArgs().length];
        for (int i = 0; i < strings.length; i++){
            strings[i] = _toString(expr.getArgs()[i],false);
        }
        _append(String.format(expr.getPattern(), strings));
    }
    
    protected void visit(DistinctPath<?> expr){
        _append("distinct ").visit(expr.getPath());
    }
    
    @Override
    protected void visit(Expr.Constant<?> expr) {
        boolean wrap = expr.getConstant().getClass().isArray();
        if (wrap) _append("(");
        _append(":a");
        if (!constants.contains(expr.getConstant())){
            constants.add(expr.getConstant());
            _append(Integer.toString(constants.size()));
        }else{
            _append(Integer.toString(constants.indexOf(expr.getConstant())+1));
        }        
        if (wrap) _append(")");
    }
        
    @Override
    protected void visit(Path.Collection<?> expr){
        // only wrap a PathCollection, if it the pathType is PROPERTY
        boolean wrap = wrapElements && expr.getMetadata().getPathType().equals(PROPERTY);
        if (wrap) _append("elements(");
        visit((Path<?>)expr);
        if (wrap) _append(")");
    }

    @Override
    protected void visit(Path<?> path) {
        PathType pathType = path.getMetadata().getPathType();
        String parentAsString = null, exprAsString = null;
        
        if (path.getMetadata().getParent() != null){
            parentAsString = _toString((Expr<?>)path.getMetadata().getParent(),false);    
        }        
        if (pathType == PROPERTY || pathType == VARIABLE ||
              pathType == LISTVALUE_CONSTANT){
            exprAsString = path.getMetadata().getExpression().toString();
        }else if (path.getMetadata().getExpression() != null){
            exprAsString = _toString(path.getMetadata().getExpression(),false);
        }
        
        String pattern = ops.getPattern(pathType);
        if (parentAsString != null){
            _append(String.format(pattern, parentAsString, exprAsString));    
        }else{
            _append(String.format(pattern, exprAsString));
        }
        
    }
    
    protected void visit(Quant q){        
        visitOperation(q.getOperator(), q.getTarget());
    }

    protected void visit(Quant.Boolean<?> q){
        visit((Quant)q);
    }
    
    protected void visit(Quant.Comparable<?> q){
        visit((Quant)q);
    }

    protected void visit(Quant.Simple<?> q){
        visit((Quant)q);
    }

    protected void visit(SubQuery<?> query) {
        QueryBase<JoinMeta,?>.Metadata md = query.getQuery().getMetadata();
        _append("(");
        serialize(md.getSelect(), md.getJoins(),
            md.getWhere(), md.getGroupBy(), md.getHaving(), 
            md.getOrderBy(), false);
        _append(")");
    }
    
    protected void visitOperation(Op<?> operator, Expr<?>... args) {    
        boolean old = wrapElements;
        wrapElements = HqlOps.wrapCollectionsForOp.contains(operator);    
        // 
        super.visitOperation(operator, args);
        //
        wrapElements = old;
    }
    
    

}
