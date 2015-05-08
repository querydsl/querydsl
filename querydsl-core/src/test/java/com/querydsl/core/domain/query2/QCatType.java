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
package com.querydsl.core.domain.query2;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import java.util.Date;

import com.querydsl.core.domain.Cat;
import com.querydsl.core.domain.QAnimal;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;


/**
 * QCat is a Querydsl query type for Cat
 */
public class QCatType extends EntityPathBase<Cat> {

    private static final long serialVersionUID = -528210988;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QCatType cat = new QCatType("cat");

    public final QAnimal _super = new QAnimal(this);

    //inherited
    public final BooleanPath alive = _super.alive;

    //inherited
    public final DateTimePath<Date> birthdate = _super.birthdate;

    //inherited
    public final NumberPath<Double> bodyWeight = _super.bodyWeight;

    public final NumberPath<Integer> breed = createNumber("breed", Integer.class);

    //inherited
    public final DatePath<java.sql.Date> dateField = _super.dateField;

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final ListPath<Cat, QCatType> kittens = this.<Cat,QCatType>createList("kittens", Cat.class, QCatType.class, PathInits.DIRECT);

    public final SetPath<Cat, QCatType> kittensSet = this.<Cat,QCatType>createSet("kittensSet", Cat.class, QCatType.class, PathInits.DIRECT);

    public final QCatType mate;

    //inherited
    public final StringPath name = _super.name;

    //inherited
    public final TimePath<java.sql.Time> timeField = _super.timeField;

    //inherited
    public final NumberPath<Integer> toes = _super.toes;

    //inherited
    public final NumberPath<Integer> weight = _super.weight;

    public QCatType(String variable) {
        this(Cat.class, forVariable(variable), INITS);
    }

    public QCatType(PathMetadata metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCatType(PathMetadata metadata, PathInits inits) {
        this(Cat.class, metadata, inits);
    }

    public QCatType(Class<? extends Cat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mate = inits.isInitialized("mate") ? new QCatType(forProperty("mate"), inits.get("mate")) : null;
    }

    public QCatType kittens(int index) {
        return kittens.get(index);
    }

    public QCatType kittens(Expression<Integer> index) {
        return kittens.get(index);
    }

}

