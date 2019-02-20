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

import com.querydsl.spatial.SpatialOps;
import com.querydsl.sql.SQLServer2012Templates;
import com.querydsl.sql.SQLTemplates;

/**
 * {@code SQLServer2012SpatialTemplates} is a spatial enabled SQL dialect for SQL Server 2012 and above
 *
 * @author Steve McDaniel
 *
 */
public class SQLServer2012SpatialTemplates extends SQLServer2012Templates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //Intentional
    public static final SQLServer2012SpatialTemplates DEFAULT = new SQLServer2012SpatialTemplates();

    public static Builder builder() {
        return new Builder() {
            @Override
            protected SQLTemplates build(char escape, boolean quote) {
                return new SQLServer2012SpatialTemplates(escape, quote);
            }
        };
    }

    public SQLServer2012SpatialTemplates() {
        this('\\',false);
    }

    public SQLServer2012SpatialTemplates(boolean quote) {
        this('\\',quote);
    }

    public SQLServer2012SpatialTemplates(char escape, boolean quote) {
        super(escape, quote);
        addCustomType(SQLServerGeometryType.DEFAULT);
        add(SpatialTemplatesSupport.getSpatialOps("ST", false));
        add(SpatialOps.X, "{0}.STX");
        add(SpatialOps.Y, "{0}.STY");
        add(SpatialOps.M, "{0}.M");
        add(SpatialOps.Z, "{0}.Z");
        add(SpatialOps.SRID, "{0}.STSrid");
    }

}