package com.mysema.query.codegen;

import com.google.common.base.Function;
import com.mysema.codegen.StringUtils;
import com.mysema.util.JavaSyntaxUtils;

/**
 * Default variable name generation strategy which un-capitalizes the first letter of the class name.
 *
 */
public final class DefaultVariableNameFunction implements Function<EntityType, String> {

    public static final DefaultVariableNameFunction INSTANCE = new DefaultVariableNameFunction();

    @Override
    public String apply(EntityType entity) {
        String uncapSimpleName = StringUtils.uncapitalize(entity.getInnerType().getSimpleName());
        if (JavaSyntaxUtils.isReserved(uncapSimpleName)) {
            uncapSimpleName = uncapSimpleName + "$";
        }
        return uncapSimpleName;
    }
}
