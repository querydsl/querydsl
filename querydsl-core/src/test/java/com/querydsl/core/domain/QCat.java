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
 * QCat is a Querydsl querydsl type for Cat
 */
public class QCat extends EntityPathBase<Cat> {

    private static final long serialVersionUID = -528210988;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QCat cat = new QCat("cat");

    public final QAnimal _super = new QAnimal(this);

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

    public final ListPath<Cat, QCat> kittens = this.<Cat,QCat>createList("kittens", Cat.class, QCat.class, PathInits.DIRECT);

    public final SetPath<Cat, QCat> kittensSet = this.<Cat,QCat>createSet("kittensSet", Cat.class, QCat.class, PathInits.DIRECT);

    public final QCat mate;

    //inherited
    public final StringPath name = _super.name;

    //inherited
    public final TimePath<java.sql.Time> timeField = _super.timeField;

    //inherited
    public final NumberPath<Integer> toes = _super.toes;

    //inherited
    public final NumberPath<Integer> weight = _super.weight;

    public QCat(String variable) {
        this(Cat.class, forVariable(variable), INITS);
    }

    public QCat(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCat(PathMetadata<?> metadata, PathInits inits) {
        this(Cat.class, metadata, inits);
    }

    public QCat(Class<? extends Cat> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mate = inits.isInitialized("mate") ? new QCat(forProperty("mate"), inits.get("mate")) : null;
    }

    public QCat kittens(int index) {
        return kittens.get(index);
    }

    public QCat kittens(Expression<Integer> index) {
        return kittens.get(index);
    }

}

