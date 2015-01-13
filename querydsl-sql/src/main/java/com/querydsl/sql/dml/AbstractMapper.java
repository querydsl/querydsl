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
package com.querydsl.sql.dml;

import java.util.Map;

import com.google.common.collect.Maps;
import com.querydsl.sql.RelationalPath;
import com.querydsl.core.types.Path;

/**
 * Abstract base class for Mapper implementations
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class AbstractMapper<T> implements Mapper<T> {

    protected Map<String, Path<?>> getColumns(RelationalPath<?> path) {
        Map<String, Path<?>> columns = Maps.newHashMap();
        for (Path<?> column : path.getColumns()) {
            columns.put(column.getMetadata().getName(), column);
        }
        return columns;
    }

    protected boolean isPrimaryKeyColumn(RelationalPath<?> parent, Path<?> property) {
        return parent.getPrimaryKey() != null
            && parent.getPrimaryKey().getLocalColumns().contains(property);
    }

}
