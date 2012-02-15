package com.mysema.query.sql.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author tiwe
 *
 */
public class UntypedNullType extends AbstractType<Null> {
    
    public UntypedNullType() {
        super(Types.OTHER);
    }

    @Override
    public Class<Null> getReturnedClass() {
        return Null.class;
    }

    @Override
    public Null getValue(ResultSet rs, int startIndex) throws SQLException {
        return null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Null value) throws SQLException {
        st.setNull(startIndex, Types.NULL);
    }

}
