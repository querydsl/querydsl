/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate.sql;

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
public class HibernateSQLSerializer extends SQLSerializer{
    
    private final List<Path<?>> entityPaths = new ArrayList<Path<?>>();

    public HibernateSQLSerializer(SQLTemplates templates) {
        super(templates);
    }
    
    @Override
    public void visit(Constant<?> expr) {        
        if (!constantToLabel.containsKey(expr.getConstant())) {
            String constLabel = constantPrefix + (constantToLabel.size() + 1);
            constantToLabel.put(expr.getConstant(), constLabel);
            append(":"+constLabel);
        } else {
            append(":"+constantToLabel.get(expr.getConstant()));
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
