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
package com.querydsl.sql.spatial;

import com.querydsl.sql.types.AbstractType;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.db.oracle.Decoders;
import org.geolatte.geom.codec.db.oracle.DefaultConnectionFinder;
import org.geolatte.geom.codec.db.oracle.Encoders;
import org.geolatte.geom.codec.db.oracle.OracleJDBCTypeFactory;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;

class SDOGeometryType extends AbstractType<Geometry> {

    public static final SDOGeometryType DEFAULT = new SDOGeometryType();

    public SDOGeometryType() {
        super(Types.OTHER);
    }

    @Override
    public Class<Geometry> getReturnedClass() {
        return Geometry.class;
    }

    @Override
    @Nullable
    public Geometry getValue(ResultSet rs, int startIndex) throws SQLException {
        byte[] bytes = rs.getBytes(startIndex);
        if (bytes != null) {
            try {
                final Struct object = rs.getObject(startIndex, Struct.class);
                return Decoders.decode(object);
            } catch (Exception e) {
                throw new SQLException(e);
            }
        } else {
            return null;
        }
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Geometry value) throws SQLException {
        try {
            final Struct struct = Encoders.encode(value, st.getConnection(), new OracleJDBCTypeFactory(new DefaultConnectionFinder()));
            st.setObject(startIndex, struct);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

}
