package com.querydsl.codegen;

import com.google.common.base.Function;
import com.mysema.codegen.StringUtils;

/**
 * Default variable name generation strategy which un-capitalizes the first letter of the variable name.
 *
 */
public class DefaultVariableNameFunction implements Function<EntityType, String> {

    @Override
    public String apply(EntityType entity) {
        return StringUtils.uncapitalize(entity.getInnerType().getSimpleName());
    }
}
