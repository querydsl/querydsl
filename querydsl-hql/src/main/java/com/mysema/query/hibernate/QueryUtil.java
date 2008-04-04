/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hibernate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.mysema.query.CascadingBoolean;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathMetadata;


/**
 * QueryUtil provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QueryUtil {
    
    public static void setConstants(Query query, List<?> constants){
        for (int i=0; i < constants.size(); i++){
            String key = "a"+(i+1);
            Object val = constants.get(i);            
            if (val instanceof Collection<?>){
                query.setParameterList(key,(Collection<?>)val);
            }else if (val.getClass().isArray()){
                query.setParameterList(key,(Object[])val);
            }else{
                query.setParameter(key,val);    
            }
        }
    }

    public static Expr.Boolean createQBECondition(Path.Entity<?> entity,
            Map<String, Object> map) {
        CascadingBoolean expr = new CascadingBoolean();  
        for (Map.Entry<String, Object> entry : map.entrySet()){                
            PathMetadata<String> md = PathMetadata.forProperty(entity, entry.getKey());
            Path.Literal<Object> path = new Path.Literal<Object>(Object.class, md);
            if (entry.getValue() != null){
                expr.and(path.eq(entry.getValue()));
            }else{
                expr.and(path.isnull());                        
            }                    
        } 
        return expr.self();
    }

}
