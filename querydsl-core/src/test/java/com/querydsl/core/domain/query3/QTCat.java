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
package com.querydsl.core.domain.query3;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import com.querydsl.core.domain.Cat;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.*;


/**
 * QCat is a Querydsl query type for Cat
 */
public class QTCat extends EntityPathBase<Cat> {

    private static final long serialVersionUID = -528210988;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QTCat cat = new QTCat("cat");

    public final QTAnimal _super = new QTAnimal(this);

    //inherited
    public final BooleanPath alive = _super.alive;

    //inherited
    public final DateTimePath<java.util.Date> birthdate = _super.birthdate;

    //inherited
    public final NumberPath<Double> bodyWeight = _super.bodyWeight;

    public final NumberPath<Integer> breed = createNumber("breed", Integer.class);

    //inherited
    public final DatePath<java.sql.Date> dateField = _super.dateField;

    //inherited
    public final NumberPath<Integer> id = _super.id;

    public final ListPath<Cat, QTCat> kittens = this.<Cat,QTCat>createList("kittens", Cat.class, QTCat.class, PathInits.DIRECT);

    public final SetPath<Cat, QTCat> kittensSet = this.<Cat,QTCat>createSet("kittensSet", Cat.class, QTCat.class, PathInits.DIRECT);

    public final QTCat mate;

    //inherited
    public final StringPath name = _super.name;

    //inherited
    public final TimePath<java.sql.Time> timeField = _super.timeField;

    //inherited
    public final NumberPath<Integer> toes = _super.toes;

    //inherited
    public final NumberPath<Integer> weight = _super.weight;

    public QTCat(String variable) {
        this(Cat.class, forVariable(variable), INITS);
    }

    public QTCat(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTCat(PathMetadata<?> metadata, PathInits inits) {
        this(Cat.class, metadata, inits);
    }

    public QTCat(Class<? extends Cat> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mate = inits.isInitialized("mate") ? new QTCat(forProperty("mate"), inits.get("mate")) : null;
    }

    public QTCat kittens(int index) {
        return kittens.get(index);
    }

    public QTCat kittens(Expression<Integer> index) {
        return kittens.get(index);
    }

}

