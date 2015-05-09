package com.querydsl.core.domain;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSuperclass is a Querydsl query type for Superclass
 */
@Generated("com.querydsl.codegen.EntitySerializer")
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

    public QSuperclass(PathMetadata metadata) {
        super(Superclass.class, metadata);
    }

}

