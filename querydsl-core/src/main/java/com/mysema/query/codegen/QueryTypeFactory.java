package com.mysema.query.codegen;

import com.mysema.codegen.model.Type;

/**
 * @author tiwe
 *
 */
public interface QueryTypeFactory {

    public Type create(Type type);

}