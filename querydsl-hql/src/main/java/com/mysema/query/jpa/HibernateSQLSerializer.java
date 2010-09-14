/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.Constant;
import com.mysema.query.types.Path;

/**
 * HibernateSQLSerializer extends the SQLSerializer to extract referenced entity paths and change some
 * serialization formats 
 * 
 * @author tiwe
 *
 */
public final class HibernateSQLSerializer extends SQLSerializer{

    private final List<Path<?>> entityPaths = new ArrayList<Path<?>>();

    public HibernateSQLSerializer(SQLTemplates templates) {
        super(templates);
    }

    @Override
    public Void visit(Constant<?> expr, Void context) {
        if (!getConstantToLabel().containsKey(expr.getConstant())) {
            String constLabel = getConstantPrefix() + (getConstantToLabel().size() + 1);
            getConstantToLabel().put(expr.getConstant(), constLabel);
            append(":"+constLabel);
        } else {
            append(":"+getConstantToLabel().get(expr.getConstant()));
        }
        return null;
    }

    @Override
    public Void visit(Path<?> path, Void context) {
        if (path.getMetadata().getParent() == null && !path.getType().equals(path.getClass())){
            super.visit(path, context);
            append(".*");
            entityPaths.add(path);
        }else{
            super.visit(path, context);
        }
        return null;
    }

    public List<Path<?>> getEntityPaths() {
        return entityPaths;
    }

}
