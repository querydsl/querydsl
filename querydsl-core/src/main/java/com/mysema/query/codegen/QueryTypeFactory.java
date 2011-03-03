package com.mysema.query.codegen;

import com.mysema.codegen.model.Type;

/**
 * @author tiwe
 *
 */
public interface QueryTypeFactory {

    public static final QueryTypeFactory DEFAULT = new QueryTypeFactoryImpl("Q", "");

    public Type create(Type type);

}