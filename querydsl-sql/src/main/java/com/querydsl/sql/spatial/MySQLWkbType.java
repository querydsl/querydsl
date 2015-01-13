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

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.querydsl.sql.types.AbstractType;
import org.geolatte.geom.ByteBuffer;
import org.geolatte.geom.ByteOrder;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.Wkb;
import org.geolatte.geom.codec.WkbDecoder;
import org.geolatte.geom.codec.WkbEncoder;
import org.geolatte.geom.codec.Wkt;

/**
 * @author tiwe
 *
 */
public class MySQLWkbType extends AbstractType<Geometry> {

    public static final MySQLWkbType DEFAULT = new MySQLWkbType();

    private final ByteOrder byteOrder = ByteOrder.NDR;

    public MySQLWkbType() {
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
            byte[] wkb = new byte[bytes.length - 4];
            System.arraycopy(bytes, 4, wkb, 0, wkb.length);
            int srid = bytes[3] << 24 | (bytes[2] & 0xff) << 16 | (bytes[1] & 0xff) << 8 | (bytes[0] & 0xff);
            // TODO make sure srid is set
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
        int srid = value.getSRID();

        // prepend srid into first 4 bytes
        byte[] wkb = buffer.toByteArray();
        byte[] bytes = new byte[wkb.length + 4];
        bytes[3] = (byte) ((srid >> 24) & 0xFF);
        bytes[2] = (byte) ((srid >> 16) & 0xFF);
        bytes[1] = (byte) ((srid >> 8) & 0xFF);
        bytes[0] = (byte) (srid & 0xFF);
        System.arraycopy(wkb, 0, bytes, 4, wkb.length);

        st.setBytes(startIndex, bytes);
    }

    @Override
    public String getLiteral(Geometry geometry) {
        String str = Wkt.newEncoder(Wkt.Dialect.POSTGIS_EWKT_1).encode(geometry);
        if (geometry.getSRID() > -1) {
            return "GeomFromText('" + str + "', " + geometry.getSRID() + ")";
        } else {
            return "GeomFromText('" + str + "')";
        }
    }

}
