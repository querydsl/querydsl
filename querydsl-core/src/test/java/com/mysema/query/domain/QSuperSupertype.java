package com.mysema.query.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;


/**
 * QSuperSupertype is a Querydsl query type for SuperSupertype
 */
public class QSuperSupertype extends EntityPathBase<SuperSupertype> {

    private static final long serialVersionUID = 518341775;

    public static final QSuperSupertype superSupertype = new QSuperSupertype("superSupertype");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QSuperSupertype(String variable) {
        super(SuperSupertype.class, forVariable(variable));
    }

    public QSuperSupertype(BeanPath<? extends SuperSupertype> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QSuperSupertype(PathMetadata<?> metadata) {
        super(SuperSupertype.class, metadata);
    }

}

