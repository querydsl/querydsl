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
package com.querydsl.jpa.impl;

import javax.persistence.Parameter;
import javax.persistence.Query;
import java.util.Map;

import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.ParamNotSetException;
import com.querydsl.core.types.expr.Param;
import com.querydsl.core.util.MathUtils;

/**
 * JPAUtil provides static utility methods for JPA
 *
 * @author tiwe
 *
 */
public final class JPAUtil {

    private JPAUtil() {}

    public static void setConstants(Query query, Map<Object,String> constants, Map<ParamExpression<?>, Object> params) {
        boolean hasParameters = !query.getParameters().isEmpty();
        for (Map.Entry<Object,String> entry : constants.entrySet()) {
            String key = entry.getValue();
            Object val = entry.getKey();
            if (Param.class.isInstance(val)) {
                val = params.get(val);
                if (val == null) {
                    throw new ParamNotSetException((Param<?>) entry.getKey());
                }
            }
            if (hasParameters) {
                Parameter parameter = query.getParameter(Integer.parseInt(key));
                Class parameterType = parameter != null ? parameter.getParameterType() : null;
                if (parameterType != null && !parameterType.isInstance(val)) {
                    if (val instanceof Number && Number.class.isAssignableFrom(parameterType)) {
                        val = MathUtils.cast((Number) val, parameterType);
                    }
                }
            }
            query.setParameter(Integer.valueOf(key), val);
        }
    }

}
