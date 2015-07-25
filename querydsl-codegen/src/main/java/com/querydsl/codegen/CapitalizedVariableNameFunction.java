package com.querydsl.codegen;

import com.google.common.base.Function;
import com.mysema.codegen.StringUtils;

/**
 * Variable name generation strategy which capitalizes the first letter of the variable name.
 *
 */
public class CapitalizedVariableNameFunction implements Function<EntityType, String> {

    @Override
    public String apply(EntityType entity) {
        return StringUtils.capitalize(entity.getInnerType().getSimpleName());
    }
}
