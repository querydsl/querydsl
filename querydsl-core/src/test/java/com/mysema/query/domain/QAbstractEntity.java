package com.mysema.query.domain;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;


/**
 * QAnimal is a Querydsl query type for Animal
 */
@SuppressWarnings("unchecked")
public class QAbstractEntity extends EntityPathBase<AbstractEntity> {

    private static final long serialVersionUID = 781156670;

    public static final QAbstractEntity animal = new QAbstractEntity("abstractEntity");
    
    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QAbstractEntity(String variable) {
        super(AbstractEntity.class, forVariable(variable));
    }

    public QAbstractEntity(BeanPath<? extends AbstractEntity<?>> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QAbstractEntity(PathMetadata<?> metadata) {
        super(AbstractEntity.class, metadata);
    }

}

