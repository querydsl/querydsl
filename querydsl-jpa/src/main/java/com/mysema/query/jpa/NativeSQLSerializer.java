/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.QTuple;

/**
 * NativeSQLSerializer extends the SQLSerializer class to extract referenced entity paths and change 
 * some serialization formats 
 * 
 * @author tiwe
 *
 */
public final class NativeSQLSerializer extends SQLSerializer {

    private final List<Path<?>> entityPaths = new ArrayList<Path<?>>();

    public NativeSQLSerializer(SQLTemplates templates) {
        super(templates);
    }
    
    @Override
    public void serialize(QueryMetadata metadata, boolean forCountRow) {
        // TODO get rid of this wrapping when Hibernate doesn't require unique aliases anymore
        int size = metadata.getProjection().size();
        Expression<?>[] args = metadata.getProjection().toArray(new Expression<?>[size]);
        boolean modified = false;
        Set<String> used = new HashSet<String>();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Path) {
                Path<?> path = (Path<?>)args[i];
                if (!used.add(path.getMetadata().getName())) {
                    args[i] = ExpressionUtils.as(args[i], "col__"+(i+1));
                    modified = true;    
                }
            } else if (args[i] instanceof FactoryExpression) {   
                FactoryExpression<?> factoryExpr = (FactoryExpression<?>)args[i];
                List<Expression<?>> fargs = Lists.newArrayList(factoryExpr.getArgs());
                for (int j = 0; j < fargs.size(); j++) {
                    if (!isAlias(fargs.get(j)) && !fargs.get(j).toString().contains("*")) {
                        fargs.set(j, ExpressionUtils.as(fargs.get(j), "col__"+(i+1)+"_"+(j+1)));
                    }
                }
                args[i] = new QTuple(ImmutableList.copyOf(fargs));
                modified = true;
                
            } else if (!isAlias(args[i])) {    
                // https://github.com/mysema/querydsl/issues/80
                if (!args[i].toString().contains("*")) {
                    args[i] = ExpressionUtils.as(args[i], "col__"+(i+1));
                    modified = true;    
                }                
            }
        }        
        if (modified) {
            metadata = metadata.clone();
            metadata.clearProjection();
            for (Expression<?> arg : args) {
                metadata.addProjection(arg);    
            }                
        }        
        super.serialize(metadata, forCountRow);
    }
    
    private boolean isAlias(Expression<?> expr) {
        return expr instanceof Operation && ((Operation<?>)expr).getOperator() == Ops.ALIAS;
    }

    @Override
    public void visitConstant(Object constant) {
        if (constant instanceof Collection<?>) {
            append("(");
            boolean first = true;
            for (Object element : ((Collection<?>)constant)) {
                if (!first) {
                    append(", ");
                }
                visitConstant(element);
                first = false;
            }            
            append(")");
        } else if (!getConstantToLabel().containsKey(constant)) {
            String constLabel = String.valueOf(getConstantToLabel().size() + 1);
            getConstantToLabel().put(constant, constLabel);
            append("?"+constLabel);
        } else {
            append("?"+getConstantToLabel().get(constant));
        }
    }
    
    @Override
    public Void visit(Path<?> path, Void context) {
        if (path.getMetadata().getParent() == null && path.getType().isAnnotationPresent(Entity.class)) {
            super.visit(path, context);
            if (stage == Stage.SELECT) {
                append(".*");    
            }            
            entityPaths.add(path);
        } else {
            super.visit(path, context);
        }
        return null;
    }

    public List<Path<?>> getEntityPaths() {
        return entityPaths;
    }

}
