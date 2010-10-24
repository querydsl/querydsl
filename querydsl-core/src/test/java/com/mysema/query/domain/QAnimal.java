package com.mysema.query.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;


/**
 * QAnimal is a Querydsl query type for Animal
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

