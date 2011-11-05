/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.types;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author tiwe
 *
 */
public class BlobType extends AbstractType<Blob> {

    public BlobType() {
        super(Types.BLOB);
    }

    public BlobType(int type) {
        super(type);
    }

    @Override
    public Blob getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getBlob(startIndex);
    }

    @Override
    public Class<Blob> getReturnedClass() {
        return Blob.class;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Blob value) throws SQLException {
        st.setBlob(startIndex, value);
    }

}
