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
package com.mysema.query.sql.spatial;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.annotation.Nullable;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.codec.WktDecoder;
import org.geolatte.geom.codec.WktEncoder;

import com.mysema.query.sql.types.AbstractType;

/**
 * @author tiwe
 *
 */
public class GeometryWktType extends AbstractType<Geometry> {

    private static final WktEncoder<Geometry> ENCODER = Wkt.newWktEncoder(Wkt.Dialect.POSTGIS_EWKT_1);

    private static final WktDecoder<Geometry> DECODER = Wkt.newWktDecoder(Wkt.Dialect.POSTGIS_EWKT_1);

    public static final GeometryWktType DEFAULT = new GeometryWktType(Types.VARCHAR, ENCODER, DECODER);

    private final int type;

    private final WktEncoder<Geometry> encoder;

    private final WktDecoder<Geometry> decoder;

    public GeometryWktType(int type) {
        this(type, ENCODER, DECODER);
    }

    public GeometryWktType(int type, WktEncoder<Geometry> encoder, WktDecoder<Geometry> decoder) {
        super(type);
        this.type = type;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    public Class<Geometry> getReturnedClass() {
        return Geometry.class;
    }

    @Override
    @Nullable
    public Geometry getValue(ResultSet rs, int startIndex) throws SQLException {
        String str;
        if (type == Types.VARCHAR) {
            str = rs.getString(startIndex);
        } else if (type == Types.CLOB) {
            Clob clob = rs.getClob(startIndex);
            str = clob != null ? clob.getSubString(1, (int) clob.length()) : null;
        } else {
            throw new IllegalStateException("Unsupported type " + type);
        }
        return str != null ? decoder.decode(str) : null;
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Geometry value) throws SQLException {
        String str = encoder.encode(value);
        if (type == Types.VARCHAR) {
            st.setString(startIndex, str);
        } else if (type == Types.CLOB) {
            st.setString(startIndex, str);
//            throw new UnsupportedOperationException();
        } else {
            throw new IllegalStateException("Unsupported type " + type);
        }
    }

}
