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
package com.querydsl.jdo.dml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.dml.DeleteClause;
import com.querydsl.jdo.JDOQLSerializer;
import com.querydsl.jdo.JDOQLTemplates;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;

/**
 * DeleteClause implementation for JDO
 *
 * @author tiwe
 *
 */
public class JDODeleteClause implements DeleteClause<JDODeleteClause> {

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    private final PersistenceManager persistenceManager;

    private final JDOQLTemplates templates;

    private final EntityPath<?> entity;

    public JDODeleteClause(PersistenceManager pm, EntityPath<?> entity) {
        this(pm, entity, JDOQLTemplates.DEFAULT);
    }

    public JDODeleteClause(PersistenceManager persistenceManager, EntityPath<?> entity, 
            JDOQLTemplates templates) {
        this.entity = entity;
        this.persistenceManager = persistenceManager;
        this.templates = templates;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    @Override
    public long execute() {
        Query query = persistenceManager.newQuery(entity.getType());
        if (metadata.getWhere() != null) {
            JDOQLSerializer serializer = new JDOQLSerializer(templates, entity);
            serializer.handle(metadata.getWhere());
            query.setFilter(serializer.toString());
            Map<Object,String> constToLabel = serializer.getConstantToLabel();

            try{
                if (!constToLabel.isEmpty()) {
                    List<Object> constants = new ArrayList<Object>(constToLabel.size());
                    StringBuilder builder = new StringBuilder();
                    for (Map.Entry<Object, String> entry : constToLabel.entrySet()) {
                        if (builder.length() > 0) {
                            builder.append(", ");
                        }
                        builder.append(entry.getKey().getClass().getName()).append(" ");
                        builder.append(entry.getValue());
                        constants.add(entry.getKey());
                    }
                    query.declareParameters(builder.toString());
                    return query.deletePersistentAll(constants.toArray());
                } else {
                    return query.deletePersistentAll();
                }
            }finally{
                query.closeAll();
            }
        } else {
            try{
                return query.deletePersistentAll();
            }finally{
                query.closeAll();
            }
        }
    }
    
    @Override
    public JDODeleteClause where(Predicate... o) {
        for (Predicate p : o) {
            metadata.addWhere(p);    
        }        
        return this;
    }
    
    @Override
    public String toString() {
        JDOQLSerializer serializer = new JDOQLSerializer(templates, entity);
        serializer.handle(metadata.getWhere());
        return serializer.toString();
    }

}
