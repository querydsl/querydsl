package com.querydsl.codegen;

import com.google.common.base.Function;
import com.mysema.codegen.StringUtils;
import com.querydsl.core.util.JavaSyntaxUtils;

/**
 * Variable name generation strategy which capitalizes the first letter of the variable name.
 *
 */
public class CapitalizedVariableNameFunction implements Function<EntityType, String> {

    @Override
    public String apply(EntityType entity) {
        String capSimpleName = StringUtils.capitalize(entity.getInnerType().getSimpleName());
        if (JavaSyntaxUtils.isReserved(capSimpleName)) {
           capSimpleName = capSimpleName + "$";
        }

        int escapeSuffix = 1;
        String capSimpleNameBase = capSimpleName;
        for (Property property : entity.getProperties()) {
           if (property.getName().equals(capSimpleName) || property.getEscapedName().equals(capSimpleName)) {
                do {
                   capSimpleName = capSimpleNameBase + (escapeSuffix++);
                } while (entity.getPropertyNames().contains(capSimpleName));
           }
        }
        return capSimpleName;
    }
}
