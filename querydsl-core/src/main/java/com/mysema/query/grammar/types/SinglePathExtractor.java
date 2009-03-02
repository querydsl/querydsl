/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;



/**
 * PathExtractor is a visitor implementation which returns the top level path from a single Expr instance
 *
 * @author tiwe
 * @version $Id$
 */
public class SinglePathExtractor extends DeepVisitor<SinglePathExtractor>{

    private Path<?> path;

    @Override
    protected void visit(Path<?> expr) {
        path = expr.getRoot();         
    }
    
    public Path<?> getPath(){
        return path;
    }

}
