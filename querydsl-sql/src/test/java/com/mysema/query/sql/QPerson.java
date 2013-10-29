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
package com.mysema.query.sql;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.EnumPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

//@Table("PERSON")
public class QPerson extends RelationalPathBase<QPerson> {

    private static final long serialVersionUID = 475064746;

    public static final QPerson person = new QPerson("PERSON");

    public final StringPath firstname = createString("firstname");

    public final EnumPath<com.mysema.query.alias.Gender> gender = createEnum("gender", com.mysema.query.alias.Gender.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath securedid = createString("securedid");

    public final PrimaryKey<QPerson> sysIdx118 = createPrimaryKey(id);

    public QPerson(String variable) {
        super(QPerson.class, PathMetadataFactory.forVariable(variable), null, "PERSON");
    }

    public QPerson(BeanPath<? extends QPerson> entity) {
        super(entity.getType(), entity.getMetadata(), null, "PERSON");
    }

    public QPerson(PathMetadata<?> metadata) {
        super(QPerson.class, metadata, null, "PERSON");
    }

    protected void addMetadata() {
        addMetadata(id, ColumnMetadata.named("ID"));
        addMetadata(firstname, ColumnMetadata.named("FIRSTNAME"));
        addMetadata(securedid, ColumnMetadata.named("SECUREDID"));
        addMetadata(gender, ColumnMetadata.named("GENDER"));
    }

}