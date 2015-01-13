package com.querydsl.sql.types;

import javax.annotation.Nullable;
import java.sql.*;

import com.google.common.primitives.Primitives;

/**
 *
 * @param <T>
 */
public class ArrayType<T> extends AbstractType<T> {

    private static void copy(Object source, Object target, int length) {
        // Note: System.arrayCopy doesn't handle copying from/to primitive arrays properly
        for (int i = 0; i < length; i++) {
            Object val = java.lang.reflect.Array.get(source, i);
            java.lang.reflect.Array.set(target, i, val);
        }
    }

    private final Class<T> type;

    private final String typeName;

    private final boolean convertPrimitives;

    public ArrayType(Class<T> type, String typeName) {
        super(Types.ARRAY);
        this.type = type;
        this.typeName = typeName;
        this.convertPrimitives = type.getComponentType().isPrimitive();
    }

    @Override
    public Class<T> getReturnedClass() {
        return type;
    }

    @Nullable
    @Override
    public T getValue(ResultSet rs, int startIndex) throws SQLException {
        Array arr = rs.getArray(startIndex);
        if (arr != null) {
            Object[] rv = (Object[])arr.getArray();
            if (convertPrimitives) {
                // primitives out
                Object rv2 = java.lang.reflect.Array.newInstance(type.getComponentType(), rv.length);
                copy(rv, rv2, rv.length);
                return (T) rv2;
            } else {
                return (T) rv;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, T value) throws SQLException {
        if (convertPrimitives) {
            // primitives in
            int length = java.lang.reflect.Array.getLength(value);
            Object value2 = java.lang.reflect.Array.newInstance(Primitives.wrap(type.getComponentType()), length);
            copy(value, value2, length);
            value = (T)value2;
        }
        Array arr = st.getConnection().createArrayOf(typeName, (Object[])value);
        st.setArray(startIndex, arr);
    }
}
