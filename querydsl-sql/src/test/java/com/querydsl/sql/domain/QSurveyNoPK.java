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
package com.querydsl.sql.domain;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;

//@Schema("PUBLIC")
//@Table("SURVEY")
public class QSurveyNoPK extends RelationalPathBase<QSurveyNoPK>{

    private static final long serialVersionUID = -7427577079709192842L;

    public static final QSurveyNoPK survey = new QSurveyNoPK("SURVEY");

    public final StringPath name = createString("name");

    public final StringPath name2 = createString("name2");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QSurveyNoPK(String path) {
        super(QSurveyNoPK.class, PathMetadataFactory.forVariable(path), "PUBLIC", "SURVEY");
        addMetadata();
    }

    public QSurveyNoPK(PathMetadata<?> metadata) {
        super(QSurveyNoPK.class, metadata, "PUBLIC", "SURVEY");
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(name, ColumnMetadata.named("NAME"));
        addMetadata(name2, ColumnMetadata.named("NAME2"));
        addMetadata(id, ColumnMetadata.named("ID"));
    }

}
