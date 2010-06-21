/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.jpa;

import java.util.Map;

import javax.persistence.Query;

import com.mysema.query.types.Param;
import com.mysema.query.types.ParamNotSetException;

/**
 * Utility methods for JPA
 *
 * @author tiwe
 *
 */
public final class JPAUtil {

    private JPAUtil(){}

    public static void setConstants(Query query, Map<Object,String> constants, Map<Param<?>, Object> params) {
        for (Map.Entry<Object,String> entry : constants.entrySet()){
            String key = entry.getValue();
            Object val = entry.getKey();
            if (Param.class.isInstance(val)){
                val = params.get(val);
                if (val == null){
                    throw new ParamNotSetException((Param<?>) entry.getKey());
                }
            }
            query.setParameter(key, val);
        }
    }

}
