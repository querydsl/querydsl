/*
 * Copyright 2016, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
