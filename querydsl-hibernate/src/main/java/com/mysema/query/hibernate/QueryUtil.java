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

import com.mysema.query.grammar.PathMetadata;
import com.mysema.query.grammar.Types.ExprBoolean;
import com.mysema.query.grammar.Types.PathEntity;
import com.mysema.query.grammar.Types.PathNoEntitySimple;


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

    public static List<ExprBoolean> createQBEConditions(PathEntity<?> entity,
            Map<String, Object> map) {
        List<ExprBoolean> conds = new ArrayList<ExprBoolean>(map.size());  
        for (Map.Entry<String, Object> entry : map.entrySet()){                
            PathMetadata<String> md = PathMetadata.forProperty(entity, entry.getKey());
            PathNoEntitySimple<Object> path = new PathNoEntitySimple<Object>(Object.class, md);
            if (entry.getValue() != null){
                conds.add(path.eq(entry.getValue()));
            }else{
                conds.add(path.isnull());                        
            }                    
        } 
        return conds;
    }

}
