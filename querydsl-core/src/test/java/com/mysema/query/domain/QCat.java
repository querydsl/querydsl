package com.mysema.query.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;


/**
 * QCat is a Querydsl query type for Cat
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

    public final ListPath<Cat, QCat> kittens = createList("kittens", Cat.class, QCat.class);

    public final SetPath<Cat, QCat> kittensSet = createSet("kittensSet", Cat.class, QCat.class);

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

