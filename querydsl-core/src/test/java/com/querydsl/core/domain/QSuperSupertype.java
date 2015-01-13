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
package com.querydsl.core.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.*;
import com.querydsl.core.types.path.*;


/**
 * QSuperSupertype is a Querydsl querydsl type for SuperSupertype
 */
public class QSuperSupertype extends EntityPathBase<SuperSupertype> {

    private static final long serialVersionUID = 518341775;

    public static final QSuperSupertype superSupertype = new QSuperSupertype("superSupertype");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QSuperSupertype(String variable) {
        super(SuperSupertype.class, forVariable(variable));
    }

    public QSuperSupertype(BeanPath<? extends SuperSupertype> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QSuperSupertype(PathMetadata<?> metadata) {
        super(SuperSupertype.class, metadata);
    }

}

