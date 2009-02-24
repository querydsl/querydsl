/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.types.Alias.AToPath;
import com.mysema.query.grammar.types.Expr.EConstant;


/**
 * PathExtractor is a visitor implementation which returns the top level path from a single Expr instance
 *
 * @author tiwe
 * @version $Id$
 */
public class PathExtractor extends VisitorAdapter<PathExtractor>{

    private Path<?> path;
    
    @Override
    protected void visit(Alias.ASimple<?> expr) {        
    }

    @Override
    protected void visit(AToPath expr) {        
    }

    @Override
    protected void visit(EConstant<?> expr) {        
    }

    @Override
    protected void visit(Operation<?, ?> expr) {
        for (Expr<?> arg : expr.getArgs()){
            this.handle(arg);
        }        
    }

    @Override
    protected void visit(Path<?> expr) {
        path = expr;        
        while (path.getMetadata().getParent() != null) path = path.getMetadata().getParent();
    }
    
    public Path<?> getPath(){
        return path;
    }
    
    public Expr<?> getPathAsExpression(){
        return (Expr<?>)path;
    }

    @Override
    protected void visit(Custom<?> expr) {
        
    }

}
