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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.jetbrains.annotations.Nullable;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.Wkt;
import org.postgis.PGgeometry;

import com.querydsl.sql.types.AbstractType;

class PGgeometryType extends AbstractType<Geometry> {

    public static final PGgeometryType DEFAULT = new PGgeometryType();

    PGgeometryType() {
        super(Types.STRUCT);
    }

    @Override
    public Class<Geometry> getReturnedClass() {
        return Geometry.class;
    }

    @Override
    @Nullable
    public Geometry getValue(ResultSet rs, int startIndex) throws SQLException {
        PGgeometry obj = (PGgeometry) rs.getObject(startIndex);
        if (obj == null) {
            return null;
        }
        return Wkt.newDecoder(Wkt.Dialect.POSTGIS_EWKT_1).decode(obj.getValue());
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Geometry value) throws SQLException {
        final String encode = Wkt.newEncoder(Wkt.Dialect.POSTGIS_EWKT_1).encode(value);
        PGgeometry geometry = new PGgeometry(encode);
        st.setObject(startIndex, geometry);
    }

    @Override
    public String getLiteral(Geometry geometry) {
        return "'" + Wkt.newEncoder(Wkt.Dialect.POSTGIS_EWKT_1).encode(geometry) + "'";
    }

}
