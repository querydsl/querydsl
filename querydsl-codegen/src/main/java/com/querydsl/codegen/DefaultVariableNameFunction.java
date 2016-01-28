package com.querydsl.codegen;

import javax.lang.model.SourceVersion;

import com.google.common.base.Function;
import com.mysema.codegen.StringUtils;

/**
 * Default variable name generation strategy which un-capitalizes the first letter of the class name.
 *
 */
public final class DefaultVariableNameFunction implements Function<EntityType, String> {

    public static final DefaultVariableNameFunction INSTANCE = new DefaultVariableNameFunction();

    @Override
    public String apply(EntityType entity) {
        String uncapSimpleName = StringUtils.uncapitalize(entity.getInnerType().getSimpleName());
        if (SourceVersion.isKeyword(uncapSimpleName)) {
            uncapSimpleName = uncapSimpleName + "$";
        }
        return uncapSimpleName;
    }
}
