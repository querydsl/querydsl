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
package com.querydsl.core.domain.query;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import java.sql.Date;
import java.sql.Time;

import com.querydsl.core.domain.Animal;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;


/**
 * QAnimal is a Querydsl query type for Animal
 */
public class QAnimal extends EntityPathBase<Animal> {

    private static final long serialVersionUID = 781156670;

    public static final QAnimal animal = new QAnimal("animal");

    public final BooleanPath alive = createBoolean("alive");

    public final DateTimePath<java.util.Date> birthdate = createDateTime("birthdate", java.util.Date.class);

    public final NumberPath<Double> bodyWeight = createNumber("bodyWeight", Double.class);

    public final DatePath<Date> dateField = createDate("dateField", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final TimePath<Time> timeField = createTime("timeField", java.sql.Time.class);

    public final NumberPath<Integer> toes = createNumber("toes", Integer.class);

    public final NumberPath<Integer> weight = createNumber("weight", Integer.class);

    public QAnimal(String variable) {
        super(Animal.class, forVariable(variable));
    }

    public QAnimal(BeanPath<? extends Animal> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QAnimal(PathMetadata metadata) {
        super(Animal.class, metadata);
    }

}

