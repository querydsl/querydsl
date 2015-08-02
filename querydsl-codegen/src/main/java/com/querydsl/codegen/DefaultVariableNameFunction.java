package com.querydsl.codegen;

import com.google.common.base.Function;
import com.mysema.codegen.StringUtils;
import com.querydsl.core.util.JavaSyntaxUtils;

/**
 * Default variable name generation strategy which un-capitalizes the first letter of the variable name.
 *
 */
public class DefaultVariableNameFunction implements Function<EntityType, String> {

    @Override
    public String apply(EntityType entity) {
        String uncapSimpleName = StringUtils.uncapitalize(entity.getInnerType().getSimpleName());
        if (JavaSyntaxUtils.isReserved(uncapSimpleName)) {
           uncapSimpleName = uncapSimpleName + "$";
        }
        return uncapSimpleName;
    }
}
