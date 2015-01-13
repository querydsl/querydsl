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
 * QAnimal is a Querydsl querydsl type for Animal
 */
public class QAnimal extends EntityPathBase<Animal> {

    private static final long serialVersionUID = 781156670;

    public static final QAnimal animal = new QAnimal("animal");

    public final BooleanPath alive = createBoolean("alive");

    public final DateTimePath<java.util.Date> birthdate = createDateTime("birthdate", java.util.Date.class);

    public final NumberPath<Double> bodyWeight = createNumber("bodyWeight", Double.class);

    public final DatePath<java.sql.Date> dateField = createDate("dateField", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final TimePath<java.sql.Time> timeField = createTime("timeField", java.sql.Time.class);

    public final NumberPath<Integer> toes = createNumber("toes", Integer.class);

    public final NumberPath<Integer> weight = createNumber("weight", Integer.class);

    public QAnimal(String variable) {
        super(Animal.class, forVariable(variable));
    }

    public QAnimal(BeanPath<? extends Animal> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QAnimal(PathMetadata<?> metadata) {
        super(Animal.class, metadata);
    }

}

