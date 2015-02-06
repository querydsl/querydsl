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
package com.querydsl.core.domain.query2;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import com.querydsl.core.domain.AbstractEntity;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.BeanPath;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.NumberPath;


/**
 * QAnimal is a Querydsl query type for Animal
 */
@SuppressWarnings("unchecked")
public class QAbstractEntityType extends EntityPathBase<AbstractEntity> {

    private static final long serialVersionUID = 781156670;

    public static final QAbstractEntityType animal = new QAbstractEntityType("abstractEntity");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QAbstractEntityType(String variable) {
        super(AbstractEntity.class, forVariable(variable));
    }

    public QAbstractEntityType(BeanPath<? extends AbstractEntity<?>> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QAbstractEntityType(PathMetadata metadata) {
        super(AbstractEntity.class, metadata);
    }

}

