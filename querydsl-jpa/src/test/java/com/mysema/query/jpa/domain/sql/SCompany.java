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
 * SCompany is a Querydsl query type for SCompany
 */
public class SCompany extends RelationalPathBase<SCompany> {

    private static final long serialVersionUID = 1400239892;

    public static final SCompany company = new SCompany("COMPANY");

    public final NumberPath<Integer> ceoId = createNumber("CEO_ID", Integer.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final PrimaryKey<SCompany> sql100819184432220 = createPrimaryKey(id);

    public final ForeignKey<SEmployee> fk9bdfd45d8e79ac65 = new ForeignKey<SEmployee>(this, ceoId, "ID");

    public final ForeignKey<SDepartment> _fka9601f72555fdbf0 = new ForeignKey<SDepartment>(this, id, "COMPANY_ID");

    public final ForeignKey<SUser> _fk4d495f4555fdbf0 = new ForeignKey<SUser>(this, id, "COMPANY_ID");

    public final ForeignKey<SEmployee> _fk4afd4ace555fdbf0 = new ForeignKey<SEmployee>(this, id, "COMPANY_ID");

    public SCompany(String variable) {
        super(SCompany.class, forVariable(variable), null, "COMPANY");
    }

    public SCompany(BeanPath<? extends SCompany> entity) {
        super(entity.getType(), entity.getMetadata(), null, "COMPANY");
    }

    public SCompany(PathMetadata<?> metadata) {
        super(SCompany.class, metadata, null, "COMPANY");
    }


}

