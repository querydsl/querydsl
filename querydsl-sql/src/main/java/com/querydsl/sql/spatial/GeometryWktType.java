/*
 * Copyright 2014, Mysema Ltd
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

import javax.annotation.Nullable;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.Wkt;

import com.querydsl.sql.types.AbstractType;

/**
 * @author tiwe
 *
 */
public class GeometryWktType extends AbstractType<Geometry> {

    public static final GeometryWktType DEFAULT = new GeometryWktType();

    public GeometryWktType() {
        super(Types.VARCHAR);
    }

    @Override
    public Class<Geometry> getReturnedClass() {
        return Geometry.class;
    }

    @Override
    @Nullable
    public Geometry getValue(ResultSet rs, int startIndex) throws SQLException {
        String str = rs.getString(startIndex);
        if (str != null) {
            return Wkt.newDecoder(Wkt.Dialect.POSTGIS_EWKT_1).decode(str);
        } else {
            return null;
        }
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Geometry value) throws SQLException {
        String str = Wkt.newEncoder(Wkt.Dialect.POSTGIS_EWKT_1).encode(value);
        st.setString(startIndex, str);
    }

    @Override
    public String getLiteral(Geometry geometry) {
        return "'" + Wkt.newEncoder(Wkt.Dialect.POSTGIS_EWKT_1).encode(geometry) + "'";
    }
}
