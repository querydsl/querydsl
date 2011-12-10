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
 * SDepartment is a Querydsl query type for SDepartment
 */
public class SDepartment extends RelationalPathBase<SDepartment> {

    private static final long serialVersionUID = -774771365;

    public static final SDepartment department = new SDepartment("DEPARTMENT");

    public final NumberPath<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final PrimaryKey<SDepartment> sql100819184432690 = createPrimaryKey(id);

    public final ForeignKey<SCompany> fka9601f72555fdbf0 = new ForeignKey<SCompany>(this, companyId, "ID");

    public SDepartment(String variable) {
        super(SDepartment.class, forVariable(variable), null, "DEPARTMENT");
    }

    public SDepartment(BeanPath<? extends SDepartment> entity) {
        super(entity.getType(), entity.getMetadata(), null, "DEPARTMENT");
    }

    public SDepartment(PathMetadata<?> metadata) {
        super(SDepartment.class, metadata, null, "DEPARTMENT");
    }

}

