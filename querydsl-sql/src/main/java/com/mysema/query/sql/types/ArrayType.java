package com.mysema.query.sql.types;

import javax.annotation.Nullable;
import java.sql.*;

/**
 *
 * @param <T>
 */
public class ArrayType<T> extends AbstractType<T> {

    private static void copy(Object source, Object target, int length) {
        for (int i = 0; i < length; i++) {
            Object val = java.lang.reflect.Array.get(source, i);
            java.lang.reflect.Array.set(target, i, val);
        }
    }

    private final Class<T> type;

    private final String typeName;

    public ArrayType(Class<T> type, String typeName) {
        super(Types.ARRAY);
        this.type = type;
        this.typeName = typeName;
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
            if (!type.isAssignableFrom(rv.getClass())) {
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
        if (!type.isAssignableFrom(value.getClass())) {

        }
        Array arr = st.getConnection().createArrayOf(typeName, (Object[])value);
        st.setArray(startIndex, arr);
    }
}
