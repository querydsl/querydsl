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
package com.querydsl.core.types.path;

import javax.annotation.Nullable;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;

/**
 * EntityPathBase provides a base class for EntityPath implementations
 *
 * @author tiwe
 *
 * @param <T> entity type
 */
public class EntityPathBase<T> extends BeanPath<T> implements EntityPath<T> {

    private static final long serialVersionUID = -8610055828414880996L;

    public EntityPathBase(Class<? extends T> type, String variable) {
        super(type, variable);
    }

    public EntityPathBase(Class<? extends T> type, PathMetadata<?> metadata) {
        super(type, metadata);
    }

    public EntityPathBase(Class<? extends T> type, PathMetadata<?> metadata, @Nullable PathInits inits) {
        super(type, metadata, inits);
    }

    @Override
    public Object getMetadata(Path<?> property) {
        return null;
    }

}
