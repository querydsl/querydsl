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
package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SLocation is a Querydsl query type for SLocation
 */
public class SLocation extends RelationalPathBase<SLocation> {

    private static final long serialVersionUID = -1336395778;

    public static final SLocation location = new SLocation("LOCATION");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final PrimaryKey<SLocation> sql100819184434890 = createPrimaryKey(id);

    public final ForeignKey<SStore> _fk4c808c12adf2d04 = new ForeignKey<SStore>(this, id, "LOCATION_ID");

    public SLocation(String variable) {
        super(SLocation.class, forVariable(variable), null, "LOCATION");
    }

    public SLocation(BeanPath<? extends SLocation> entity) {
        super(entity.getType(), entity.getMetadata(), null, "LOCATION");
    }

    public SLocation(PathMetadata<?> metadata) {
        super(SLocation.class, metadata, null, "LOCATION");
    }
    
}

