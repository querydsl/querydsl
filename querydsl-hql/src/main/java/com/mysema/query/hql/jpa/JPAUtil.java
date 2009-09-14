package com.mysema.query.hql.jpa;

import java.util.Map;

import javax.persistence.Query;

/**
 * Utility methods for JPA
 * 
 * @author tiwe
 *
 */
public final class JPAUtil {
    
    private JPAUtil(){}
    
    public static void setConstants(Query query, Map<Object,String> constants) {
        for (Map.Entry<Object,String> entry : constants.entrySet()){
            String key = entry.getValue();
            Object val = entry.getKey();
            query.setParameter(key, val);
        }
    }

}
