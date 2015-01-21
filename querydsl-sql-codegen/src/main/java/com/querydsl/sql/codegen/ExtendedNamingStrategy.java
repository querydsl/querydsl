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
package com.querydsl.sql.codegen;

import java.util.regex.Pattern;

import com.querydsl.codegen.EntityType;

/**
 * ExtendedNamingStrategy works like the DefaultNamingStrategy but tries to create foreign key 
 * property names in a different way.
 * 
 * <p>It looks for patterns like this {@literal .*_<forward>_<inverse>} and uses the forward part for
 * the foreign key name and inverse for the inverse foreign key name.</p>
 * 
 * @author tiwe
 *
 */
public class ExtendedNamingStrategy extends DefaultNamingStrategy {
    
    private static final Pattern SPLIT = Pattern.compile("_");
    
    @Override
    public String getPropertyNameForForeignKey(String fkName, EntityType entityType) {
        String[] split = SPLIT.split(fkName);
        if (split.length > 2) {
            return getPropertyName(split[split.length-2], entityType);
        } else {
            return super.getPropertyNameForForeignKey(fkName, entityType);
        }
    }

    @Override
    public String getPropertyNameForInverseForeignKey(String fkName, EntityType entityType) {
        String[] split = SPLIT.split(fkName);
        if (split.length > 2) {
            return getPropertyName(split[split.length-1], entityType);
        } else {
            return super.getPropertyNameForInverseForeignKey(fkName, entityType);
        }
    }
    
}
