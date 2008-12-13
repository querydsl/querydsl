package com.mysema.query.grammar.types;

import com.mysema.query.grammar.types.Alias.ToPath;
import com.mysema.query.grammar.types.Expr.Constant;


/**
 * PathExtractor provides
 *
 * @author tiwe
 * @version $Id$
 */
public class PathExtractor extends VisitorAdapter<PathExtractor>{

    private Path<?> path;
    
    @Override
    protected void visit(Alias.Simple<?> expr) {        
    }

    @Override
    protected void visit(ToPath expr) {        
    }

    @Override
    protected void visit(Constant<?> expr) {        
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



}
