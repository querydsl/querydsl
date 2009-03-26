package com.mysema.query.serialization;

import java.util.Arrays;

import com.mysema.query.grammar.types.AbstractVisitor;
import com.mysema.query.grammar.types.Custom;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Operation;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.Alias.ASimple;
import com.mysema.query.grammar.types.Alias.AToPath;
import com.mysema.query.grammar.types.Expr.EArrayConstructor;
import com.mysema.query.grammar.types.Expr.EConstant;
import com.mysema.query.grammar.types.Expr.EConstructor;

/**
 * ToStringVisitor provides
 *
 * @author tiwe
 * @version $Id$
 */
public class ToStringVisitor extends AbstractVisitor<ToStringVisitor>{

    private static OperationPatterns ops = new OperationPatterns(){{
        // TODO
    }};
    
    private String toString = "?";
    
    protected void visit(EConstructor<?> e){
        StringBuilder builder = new StringBuilder();
        builder.append("new ").append(e.getType().getSimpleName()).append("(");
        for (int i=0; i < e.getArgs().length; i++){
            if (i > 0) builder.append(", ");
            builder.append(e.getArgs()[i]);
        }
        builder.append(")");
        toString = builder.toString();
    }
    
    protected void visit(EArrayConstructor<?> e){
        StringBuilder builder = new StringBuilder("[");
        for (int i=0; i < e.getArgs().length; i++){
            if (i > 0) builder.append(", ");
            builder.append(e.getArgs()[i]);
        }
        builder.append("]");
        toString = builder.toString();
    }
    
    protected void visit(EConstant<?> e){
        toString = e.getConstant().toString();
    }
    
    protected void visit(Path<?> p){
        Path<?> parent = p.getMetadata().getParent();
        Expr<?> expr = p.getMetadata().getExpression();
        if (parent != null){
            toString = String.format(ops.getPattern(p.getMetadata().getPathType()), parent, expr); 
        }else if (expr != null){
            toString =  expr.toString();
        }
    }
    
    protected void visit(Operation<?,?> o){
        String pattern = ops.getPattern(o.getOperator());
        if (pattern != null){
            toString =  String.format(pattern, o.getArgs());
        }else{
            toString = "unknown operation with args " + Arrays.asList(o.getArgs());
        }
    }
    
    public String toString(){
        return toString;
    }

    @Override
    protected void visit(ASimple<?> expr) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void visit(AToPath expr) {
        // TODO Auto-generated method stub        
    }

    @Override
    protected void visit(Custom<?> expr) {
        // TODO Auto-generated method stub
        
    }

}
