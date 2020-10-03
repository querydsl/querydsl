package com.querydsl.core.domain.query2;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import com.querydsl.core.annotations.Generated;
import com.querydsl.core.domain.IdNamePair;
import com.querydsl.core.domain.QIdNamePair;
import com.querydsl.core.domain.Superclass;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSuperclass is a Querydsl query type for Superclass
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSuperclassType extends EntityPathBase<Superclass> {

    private static final long serialVersionUID = -1300377102;

    public static final QSuperclassType superclass = new QSuperclassType("superclass");

    public final ListPath<IdNamePair<String>, QIdNamePair> fooOfSuperclass = this.<IdNamePair<String>, QIdNamePair>createList("fooOfSuperclass", IdNamePair.class, QIdNamePair.class, PathInits.DIRECT);

    public QSuperclassType(String variable) {
        super(Superclass.class, forVariable(variable));
    }

    public QSuperclassType(Path<? extends Superclass> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QSuperclassType(PathMetadata metadata) {
        super(Superclass.class, metadata);
    }

}

