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

import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.Wkt;

import com.mysema.query.sql.H2Templates;
import com.mysema.query.sql.SQLTemplates;

/**
 * GeoDBTemplates is a spatial enabled SQL dialect for GeoDB
 *
 * @author tiwe
 *
 */
public class GeoDBTemplates extends H2Templates {

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new GeoDBTemplates(escape, quote);
            }
        };
    }

    public GeoDBTemplates() {
        this('\\', false);
    }

    public GeoDBTemplates(boolean quote) {
        this('\\', quote);
    }

    public GeoDBTemplates(char escape, boolean quote) {
        super(escape, quote);
        addCustomType(GeoDBWkbType.DEFAULT);
        add(SpatialTemplatesSupport.getSpatialOps(true));
    }

    @Override
    public String asLiteral(Object o) {
        if (o instanceof Geometry) {
            Geometry geometry = (Geometry)o;
            String str = Wkt.newWktEncoder(Wkt.Dialect.POSTGIS_EWKT_1).encode(geometry);
            if (geometry.getSRID() > -1) {
                return "ST_GeomFromText('" + str + "', " + geometry.getSRID() + ")";
            } else {
                return "ST_GeomFromText('" + str + "')";
            }
        } else {
            return super.asLiteral(o);
        }
    }

}