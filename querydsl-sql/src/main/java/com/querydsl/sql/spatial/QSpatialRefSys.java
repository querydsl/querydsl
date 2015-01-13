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

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;



/**
 * QSpatialRefSys is a Querydsl querydsl type for SpatialRefSys
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QSpatialRefSys extends RelationalPathSpatial<SpatialRefSys> {

    private static final long serialVersionUID = 1681874658;

    public static final QSpatialRefSys spatialRefSys = new QSpatialRefSys("spatial_ref_sys");

    public final StringPath authName = createString("authName");

    public final NumberPath<Integer> authSrid = createNumber("authSrid", Integer.class);

    public final NumberPath<Integer> srid = createNumber("srid", Integer.class);

    public final StringPath srtext = createString("srtext");

    public final com.querydsl.sql.PrimaryKey<SpatialRefSys> spatialRefSysPkey = createPrimaryKey(srid);

    public QSpatialRefSys(String variable) {
        super(SpatialRefSys.class, forVariable(variable), "public", "spatial_ref_sys");
        addMetadata();
    }

    public QSpatialRefSys(String variable, String schema, String table) {
        super(SpatialRefSys.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QSpatialRefSys(Path<? extends SpatialRefSys> path) {
        super(path.getType(), path.getMetadata(), "public", "spatial_ref_sys");
        addMetadata();
    }

    public QSpatialRefSys(PathMetadata<?> metadata) {
        super(SpatialRefSys.class, metadata, "public", "spatial_ref_sys");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(authName, ColumnMetadata.named("auth_name").ofType(12).withSize(256));
        addMetadata(authSrid, ColumnMetadata.named("auth_srid").ofType(4).withSize(10));
        addMetadata(srid, ColumnMetadata.named("srid").ofType(4).withSize(10).notNull());
        addMetadata(srtext, ColumnMetadata.named("srtext").ofType(12).withSize(2048));
    }

}

