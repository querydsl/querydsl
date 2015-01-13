package com.querydsl.sql.codegen;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import com.querydsl.codegen.Property;
import com.querydsl.sql.ColumnMetadata;

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
