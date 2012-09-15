package com.mysema.query.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSuperclass is a Querydsl query type for Superclass
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSuperclass extends EntityPathBase<Superclass> {

    private static final long serialVersionUID = -1300377102;

    public static final QSuperclass superclass = new QSuperclass("superclass");

    public final ListPath<IdNamePair<String>, QIdNamePair> fooOfSuperclass = this.<IdNamePair<String>, QIdNamePair>createList("fooOfSuperclass", IdNamePair.class, QIdNamePair.class, PathInits.DIRECT);

    public QSuperclass(String variable) {
        super(Superclass.class, forVariable(variable));
    }

    public QSuperclass(Path<? extends Superclass> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QSuperclass(PathMetadata<?> metadata) {
        super(Superclass.class, metadata);
    }

}

