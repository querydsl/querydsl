/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.expr.Constant;
import com.mysema.query.types.path.Path;

/**
 * @author tiwe
 *
 */
public final class HibernateSQLSerializer extends SQLSerializer{
    
    private final List<Path<?>> entityPaths = new ArrayList<Path<?>>();

    public HibernateSQLSerializer(SQLTemplates templates) {
        super(templates);
    }
    
    @Override
    public void visit(Constant<?> expr) {        
        if (!getConstantToLabel().containsKey(expr.getConstant())) {
            String constLabel = getConstantPrefix() + (getConstantToLabel().size() + 1);
            getConstantToLabel().put(expr.getConstant(), constLabel);
            append(":"+constLabel);
        } else {
            append(":"+getConstantToLabel().get(expr.getConstant()));
        }
    }
    
    @Override
    public void visit(Path<?> path) {
        if (path.getMetadata().getParent() == null && !path.getType().equals(path.getClass())){
            super.visit(path);
            append(".*");
            entityPaths.add(path);
        }else{
            super.visit(path);
        }        
    }

    public List<Path<?>> getEntityPaths() {
        return entityPaths;
    }
    
    

}
