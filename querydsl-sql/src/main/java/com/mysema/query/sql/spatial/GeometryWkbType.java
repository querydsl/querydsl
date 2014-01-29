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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nullable;

import org.geolatte.geom.ByteBuffer;
import org.geolatte.geom.ByteOrder;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.WkbDecoder;
import org.geolatte.geom.codec.WkbEncoder;

import com.mysema.query.sql.types.AbstractType;

/**
 * @author tiwe
 *
 */
public class GeometryWkbType extends AbstractType<Geometry> {

    private final WkbEncoder encoder;

    private final WkbDecoder decoder;

    private final ByteOrder byteOrder;

    public GeometryWkbType(int type, WkbEncoder encoder, WkbDecoder decoder, ByteOrder byteOrder) {
        super(type);
        this.encoder = encoder;
        this.decoder = decoder;
        this.byteOrder = byteOrder;
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
            return decoder.decode(ByteBuffer.from(bytes));
        } else {
            return null;
        }
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Geometry value) throws SQLException {
        ByteBuffer buffer = encoder.encode(value, byteOrder);
        st.setBytes(startIndex, buffer.toByteArray());
    }

}
