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

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;


/**
 * SSyscolumns is a Querydsl query type for SSyscolumns
 */
public class SSyscolumns extends com.mysema.query.sql.RelationalPathBase<SSyscolumns> {

    private static final long serialVersionUID = -20060127;

    public static final SSyscolumns syscolumns = new SSyscolumns("SYSCOLUMNS");

    public final NumberPath<Long> autoincrementinc = createNumber("AUTOINCREMENTINC", Long.class);

    public final NumberPath<Long> autoincrementstart = createNumber("AUTOINCREMENTSTART", Long.class);

    public final NumberPath<Long> autoincrementvalue = createNumber("AUTOINCREMENTVALUE", Long.class);

    public final SimplePath<Object> columndatatype = createSimple("COLUMNDATATYPE", Object.class);

    public final SimplePath<Object> columndefault = createSimple("COLUMNDEFAULT", Object.class);

    public final StringPath columndefaultid = createString("COLUMNDEFAULTID");

    public final StringPath columnname = createString("COLUMNNAME");

    public final NumberPath<Integer> columnnumber = createNumber("COLUMNNUMBER", Integer.class);

    public final StringPath referenceid = createString("REFERENCEID");

    public SSyscolumns(String variable) {
        super(SSyscolumns.class, forVariable(variable), "SYS", "SYSCOLUMNS");
    }

    public SSyscolumns(Path<? extends SSyscolumns> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSCOLUMNS");
    }

    public SSyscolumns(PathMetadata<?> metadata) {
        super(SSyscolumns.class, metadata, "SYS", "SYSCOLUMNS");
    }

}

