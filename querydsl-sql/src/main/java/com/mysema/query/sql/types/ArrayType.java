package com.mysema.query.sql.types;

import javax.annotation.Nullable;
import java.sql.*;

/**
 *
 * @param <T>
 */
public class ArrayType<T> extends AbstractType<T[]> {

    private final Class<T[]> type;

    private final String typeName;

    public ArrayType(Class<T[]> type, String typeName) {
        super(Types.ARRAY);
        this.type = type;
        this.typeName = typeName;
    }

    @Override
    public Class<T[]> getReturnedClass() {
        return type;
    }

    @Nullable
    @Override
    public T[] getValue(ResultSet rs, int startIndex) throws SQLException {
        Array arr = rs.getArray(startIndex);
        return arr != null ? (T[])arr.getArray() : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, T[] value) throws SQLException {
        Array arr = st.getConnection().createArrayOf(typeName, value);
        st.setArray(startIndex, arr);
    }
}
