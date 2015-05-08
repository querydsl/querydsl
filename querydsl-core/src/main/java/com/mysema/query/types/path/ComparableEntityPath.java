/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.mysema.query.types.path;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;

/**
 * ComparableEntityPath extends the ComparablePath class to implement the EntityPath interface
 *
 * @author tiwe
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class ComparableEntityPath<T extends Comparable> extends ComparablePath<T> implements EntityPath<T> {

    private static final long serialVersionUID = -7115848171352092315L;

    public ComparableEntityPath(Class<? extends T> type, Path<?> parent, String property) {
        super(type, parent, property);
    }

    public ComparableEntityPath(Class<? extends T> type, PathMetadata<?> metadata) {
        super(type, metadata);
    }

    public ComparableEntityPath(Class<? extends T> type, String var) {
        super(type, var);
    }

    @Override
    public Object getMetadata(Path<?> property) {
        return null;
    }

}
