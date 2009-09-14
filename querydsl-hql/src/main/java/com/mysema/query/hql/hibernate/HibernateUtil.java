package com.mysema.query.hql.hibernate;

import java.util.Collection;
import java.util.Map;

import org.hibernate.Query;

/**
 * Utility methods for Hibernate
 * 
 * @author tiwe
 *
 */
public final class HibernateUtil {
    
    private HibernateUtil(){}

    public static void setConstants(Query query, Map<Object,String> constants) {
        for (Map.Entry<Object, String> entry : constants.entrySet()){
            String key = entry.getValue();
            Object val = entry.getKey();
            
            if (val instanceof Collection<?>) {
                // NOTE : parameter types should be given explicitly
                query.setParameterList(key, (Collection<?>) val);
            } else if (val.getClass().isArray()) {
                // NOTE : parameter types should be given explicitly
                query.setParameterList(key, (Object[]) val);
            } else {
                // NOTE : parameter types should be given explicitly
                query.setParameter(key, val);
            }
        }
    }
}
