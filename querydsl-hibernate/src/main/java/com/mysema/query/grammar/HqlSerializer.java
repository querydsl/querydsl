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
import com.mysema.query.grammar.HqlGrammar.Constructor;
import com.mysema.query.grammar.HqlGrammar.CountExpr;
import com.mysema.query.grammar.Types.*;

/**
 * HqlSerializer provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlSerializer extends VisitorAdapter<HqlSerializer>{
        
    private StringBuilder builder = new StringBuilder();

    private List<Object> constants = new ArrayList<Object>();
    
    private HqlSerializer _append(String str) {
        builder.append(str);
        return this;
    }

    private HqlSerializer _append(String sep, List<? extends Expr<?>> expressions) {
        boolean first = true;
        for (Expr<?> expr : expressions){
            if (!first) builder.append(sep);
            handle(expr); first = false;
        }
        return this;
    }
    
    private String _toString(Expr<?> expr) {
        StringBuilder old = builder;
        builder = new StringBuilder();
        handle(expr);
        String ret = builder.toString();
        builder = old;
        return ret;
    }

    public List<Object> getConstants(){
        return constants;
    }

    public void serialize(List<Expr<?>> select, List<JoinExpression> joins,
        List<ExprBoolean> where, List<Expr<?>> groupBy, List<ExprBoolean> having,
        List<OrderSpecifier<?>> orderBy, boolean forCountRow){
         if (forCountRow){
            _append("select count(*)\n");
        }else if (!select.isEmpty()){
            _append("select ")._append(", ", select)._append("\n");
        }        
        _append("from ");
        for (int i=0; i < joins.size(); i++){
            JoinExpression je = joins.get(i);            
            if (i > 0){
                String sep = ", ";
                if (je.getTarget() instanceof AliasToPath){
                    switch(je.getType()){
                    case FULLJOIN:  sep = "\nfull join "; break;
                    case INNERJOIN: sep = "\ninner join "; break;
                    case JOIN:      sep = "\njoin "; break;
                    case LEFTJOIN:  sep = "\nleft join "; break;                                
                    }    
                }                
                _append(sep);
            }
            // type specifier
            if (je.getTarget() instanceof PathEntity){
                PathEntity<?> pe = (PathEntity<?>)je.getTarget();
                if (pe.getMetadata().getParent() == null){
                    String pn = pe.getType().getPackage().getName();
                    String typeName = pe.getType().getName().substring(pn.length()+1); 
                    _append(typeName)._append(" ");    
                }                
            }            
            handle(je.getTarget());
            if (je.getConditions() != null){
                _append("\nwith ")._append(" and ", Arrays.asList(je.getConditions()));
            }
        }
        
        if (!where.isEmpty()){
            _append("\nwhere ")._append(" and ",where);
        }
        if (!groupBy.isEmpty()){
            _append("\ngroup by ")._append(", ",groupBy);
        }
        if (!having.isEmpty()){
            if (groupBy.isEmpty()) {
                throw new IllegalArgumentException("having, but not groupBy was given");
            }                
            _append("\nhaving ")._append(" and ", having);
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
    
    public String toString(){ return builder.toString(); }

    @Override
    protected void visit(AliasSimple expr) {
        handle(expr.getFrom())._append(" as ")._append(expr.getTo());        
    }
    
    @Override
    protected void visit(AliasToPath expr) {
        handle(expr.getFrom())._append(" as ").visit(expr.getTo());        
    }
    
    @Override
    protected void visit(ConstantExpr<?> expr) {
        _append(":a");
        if (!constants.contains(expr.getConstant())){
            constants.add(expr.getConstant());
            _append(Integer.toString(constants.size()));
        }else{
            _append(Integer.toString(constants.indexOf(expr.getConstant())+1));
        }        
    }

    protected void visit(Constructor<?> expr){
        _append("new ")._append(expr.getType().getName())._append("(");
        _append(", ",Arrays.asList(expr.args))._append(")");
    }
    
    protected void visit(CountExpr expr) {
        if (expr.target == null){
            _append("count(*)");    
        }else{
            _append("count(").handle(expr.target)._append(")");
        }                
    }
    
    @Override
    protected void visit(Operation<?,?> expr) {
        String pattern = HqlOps.getPattern(expr.getOperator());
        if (pattern == null)
            throw new IllegalArgumentException("Got no operation pattern for " + expr);
        Object[] args = new Object[expr.getArgs().length];
        for (int i = 0; i < args.length; i++){
            args[i] = _toString(expr.getArgs()[i]);
        }
        _append(String.format(pattern, args));
    }

    @Override
    protected void visit(Path<?> path) {
        if (path.getMetadata().getParent() != null){
            visit(path.getMetadata().getParent());
        }
        Expr<?> expr = path.getMetadata().getExpression();
        switch(path.getMetadata().getType()){
            case LISTACCESS :             
            case MAPACCESS : _append("[").handle(expr)._append("]"); break;
            case LISTACCESSC : _append("[")._append(expr.toString())._append("]"); break;
            case MAPACCESSC : _append("[").handle(expr)._append("]"); break;
            case PROPERTY : _append(".")._append(expr.toString()); break;
            case VARIABLE : _append(expr.toString()); break;
        }
    }

    @Override
    protected void visit(SubQuery<?> subQuery) {
        // TODO : replace this with a full sub query support
        _append("(");
        _append("\n    select ").handle(subQuery._select());
        _append("\n    from ")._append(", ", subQuery._from());
        _append("\n    where ")._append(" and ", subQuery._where());
        _append(")");        
    }

}
