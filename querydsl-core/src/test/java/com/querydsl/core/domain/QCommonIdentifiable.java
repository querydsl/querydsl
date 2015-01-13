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
 * QCommonIdentifiable is a Querydsl querydsl type for CommonIdentifiable
 */
public class QCommonIdentifiable extends EntityPathBase<CommonIdentifiable<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 1818647030;

    public static final QCommonIdentifiable commonIdentifiable = new QCommonIdentifiable("commonIdentifiable");

    public final QCommonPersistence _super = new QCommonPersistence(this);

    public final SimplePath<java.io.Serializable> id = createSimple("id", java.io.Serializable.class);

    //inherited
    public final NumberPath<Long> version = _super.version;

    @SuppressWarnings("unchecked")
    public QCommonIdentifiable(String variable) {
        super((Class)CommonIdentifiable.class, forVariable(variable));
    }

    public QCommonIdentifiable(BeanPath<? extends CommonIdentifiable<? extends java.io.Serializable>> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    @SuppressWarnings("unchecked")
    public QCommonIdentifiable(PathMetadata<?> metadata) {
        super((Class)CommonIdentifiable.class, metadata);
    }

}

