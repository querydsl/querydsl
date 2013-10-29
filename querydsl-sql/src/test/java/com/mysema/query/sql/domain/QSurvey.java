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
package com.mysema.query.sql.domain;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

//@Schema("PUBLIC")
//@Table("SURVEY")
public class QSurvey extends RelationalPathBase<QSurvey>{

    private static final long serialVersionUID = -7427577079709192842L;

    public static final QSurvey survey = new QSurvey("SURVEY");

    public final StringPath name = createString("name");

    public final StringPath name2 = createString("name2");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final PrimaryKey<QSurvey> idKey = createPrimaryKey(id);

    public QSurvey(String path) {
        super(QSurvey.class, PathMetadataFactory.forVariable(path), "PUBLIC", "SURVEY");
        addMetadata();
    }

    public QSurvey(PathMetadata<?> metadata) {
        super(QSurvey.class, metadata, "PUBLIC", "SURVEY");
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(name, ColumnMetadata.named("NAME"));
        addMetadata(name2, ColumnMetadata.named("NAME2"));
        addMetadata(id, ColumnMetadata.named("ID"));
    }

}
