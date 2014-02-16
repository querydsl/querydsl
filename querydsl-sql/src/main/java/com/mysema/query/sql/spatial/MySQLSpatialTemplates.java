/*
 * Copyright 2011, Mysema Ltd
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

import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.Wkt;

import com.mysema.query.spatial.SpatialOps;
import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.SQLTemplates;

/**
 * MySQLSpatialTemplates is a spatial enabled SQL dialect for MySQL
 *
 * @author tiwe
 *
 */
public class MySQLSpatialTemplates extends MySQLTemplates {

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new MySQLSpatialTemplates(escape, quote);
            }
        };
    }

    public MySQLSpatialTemplates() {
        this('\\', false);
    }

    public MySQLSpatialTemplates(boolean quote) {
        this('\\', quote);
    }

    public MySQLSpatialTemplates(char escape, boolean quote) {
        super(escape, quote);
        addCustomType(MySQLWkbType.DEFAULT);
        add(SpatialTemplatesSupport.getSpatialOps("", true));
        add(SpatialOps.NUM_INTERIOR_RING, "NumInteriorRings({0})");
    }

    @Override
    public String asLiteral(Object o) {
        if (o instanceof Geometry) {
            Geometry geometry = (Geometry)o;
            String str = Wkt.newWktEncoder(Wkt.Dialect.POSTGIS_EWKT_1).encode(geometry);
            if (geometry.getSRID() > -1) {
                return "GeomFromText('" + str + "', " + geometry.getSRID() + ")";
            } else {
                return "GeomFromText('" + str + "')";
            }
        } else {
            return super.asLiteral(o);
        }
    }

}
