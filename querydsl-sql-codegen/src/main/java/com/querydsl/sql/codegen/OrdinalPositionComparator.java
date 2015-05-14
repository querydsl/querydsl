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
package com.querydsl.sql.codegen;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import com.querydsl.codegen.Property;
import com.querydsl.sql.ColumnMetadata;

/**
 * Compares {@link Property} instances based on their ordinal position in the table
 */
public class OrdinalPositionComparator implements Comparator<Property> {

    public OrdinalPositionComparator() {
        super();
    }

    @Override
    public int compare(Property property1, Property property2) {
        Integer comparison = null;
        for (Property property : Arrays.asList(property1, property2)) {
            Map<Object, Object> data = property.getData();
            ColumnMetadata columnMetadata = (ColumnMetadata) data.get("COLUMN");
            int index = columnMetadata.getIndex();
            comparison = comparison == null ? index : comparison - index;
        }
        return comparison;
    }

}
