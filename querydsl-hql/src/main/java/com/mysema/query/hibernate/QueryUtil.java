/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

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

    public static List<Expr.Boolean> createQBEConditions(Path.Entity<?> entity,
            Map<String, Object> map) {
        List<Expr.Boolean> conds = new ArrayList<Expr.Boolean>(map.size());  
        for (Map.Entry<String, Object> entry : map.entrySet()){                
            PathMetadata<String> md = PathMetadata.forProperty(entity, entry.getKey());
            Path.NoEntitySimple<Object> path = new Path.NoEntitySimple<Object>(Object.class, md);
            if (entry.getValue() != null){
                conds.add(path.eq(entry.getValue()));
            }else{
                conds.add(path.isnull());                        
            }                    
        } 
        return conds;
    }

}
