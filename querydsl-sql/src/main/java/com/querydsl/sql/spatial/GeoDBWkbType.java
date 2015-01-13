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

import org.geolatte.geom.ByteBuffer;
import org.geolatte.geom.ByteOrder;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.Wkb;
import org.geolatte.geom.codec.WkbDecoder;
import org.geolatte.geom.codec.WkbEncoder;
import org.geolatte.geom.codec.Wkt;

import com.querydsl.sql.types.AbstractType;

/**
 * @author tiwe
 *
 */
public class GeoDBWkbType extends AbstractType<Geometry> {

    public static final GeoDBWkbType DEFAULT = new GeoDBWkbType();

    private final ByteOrder byteOrder = ByteOrder.NDR;

    public GeoDBWkbType() {
        super(Types.BLOB);
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
            byte[] wkb;
            if (bytes[0] != 0 && bytes[0] != 1) { // decodes EWKB
                wkb = new byte[bytes.length - 32];
                System.arraycopy(bytes, 32, wkb, 0, wkb.length);
            } else {
                wkb = bytes;
            }
            WkbDecoder decoder = Wkb.newDecoder(Wkb.Dialect.POSTGIS_EWKB_1);
            return decoder.decode(ByteBuffer.from(wkb));
        } else {
            return null;
        }
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Geometry value) throws SQLException {
        WkbEncoder encoder = Wkb.newEncoder(Wkb.Dialect.POSTGIS_EWKB_1);
        ByteBuffer buffer = encoder.encode(value, byteOrder);
        st.setBytes(startIndex, buffer.toByteArray());
    }

    @Override
    public String getLiteral(Geometry geometry) {
        String str = Wkt.newEncoder(Wkt.Dialect.POSTGIS_EWKT_1).encode(geometry);
        if (geometry.getSRID() > -1) {
            return "ST_GeomFromText('" + str + "', " + geometry.getSRID() + ")";
        } else {
            return "ST_GeomFromText('" + str + "', -1)";
        }
    }

}
