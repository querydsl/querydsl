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
package com.querydsl.sql.domain;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.StringPath;

//@Table("TEST")
public class QTestX extends EntityPathBase<Object> {

    private static final long serialVersionUID = -8421112749591552595L;

    public final StringPath name = createString("NAME");

    public QTestX(String path) {
        super(Object.class, PathMetadataFactory.forVariable(path));
    }

    public QTestX(PathMetadata metadata) {
        super(Object.class, metadata);
    }

}
