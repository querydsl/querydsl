package com.mysema.query.sql;

import java.sql.PreparedStatement;
import java.util.Collection;

import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tiwe
 *
 */
public class JDBCUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(AbstractSQLQuery.class);
    
    public static void setParameters(PreparedStatement stmt, Collection<Object> objects){
        int counter = 1;
        for (Object o : objects) {
            try {
                setParameter(stmt, counter++, o);
            } catch (Exception e) {
                String error = "Caught " + e.getClass().getName();
                logger.error(error, e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
    
    private static void setParameter(PreparedStatement stmt, int i, Object o) throws Exception {
        Class<?> type = o.getClass();
        String methodName = "set" + type.getSimpleName();
        if (methodName.equals("setInteger")) {
            methodName = "setInt";
        }
        type = ClassUtils.wrapperToPrimitive(type) != null ? ClassUtils.wrapperToPrimitive(type) : type;
        if (methodName.equals("setDate") && type.equals(java.util.Date.class)) {
            type = java.sql.Date.class;
            o = new java.sql.Date(((java.util.Date) o).getTime());
        }
        // TODO : cache methods
        PreparedStatement.class.getMethod(methodName, int.class, type).invoke(stmt, i, o);
    }

}
