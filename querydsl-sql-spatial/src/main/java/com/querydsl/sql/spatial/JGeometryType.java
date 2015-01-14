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

import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

import org.geolatte.geom.Geometry;

import com.querydsl.sql.types.AbstractType;

/**
 * @author tiwe
 *
 */
public class JGeometryType extends AbstractType<Geometry> {

    public static final JGeometryType DEFAULT = new JGeometryType();

    public JGeometryType() {
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
                JGeometry geometry = JGeometry.load(bytes);
                return JGeometryConverter.convert(geometry);
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
            JGeometry geo = JGeometryConverter.convert(value);
            STRUCT struct = JGeometry.store(st.getConnection(), geo);
            st.setObject(startIndex, struct);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

}
