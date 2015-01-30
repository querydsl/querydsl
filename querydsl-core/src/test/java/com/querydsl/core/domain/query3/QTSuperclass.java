package com.querydsl.core.domain.query3;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.domain.IdNamePair;
import com.querydsl.core.domain.Superclass;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.ListPath;
import com.querydsl.core.types.path.PathInits;


/**
 * QSuperclass is a Querydsl query type for Superclass
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTSuperclass extends EntityPathBase<Superclass> {

    private static final long serialVersionUID = -1300377102;

    public static final QTSuperclass superclass = new QTSuperclass("superclass");

    public final ListPath<IdNamePair<String>, QTIdNamePair> fooOfSuperclass = this.<IdNamePair<String>, QTIdNamePair>createList("fooOfSuperclass", IdNamePair.class, QTIdNamePair.class, PathInits.DIRECT);

    public QTSuperclass(String variable) {
        super(Superclass.class, forVariable(variable));
    }

    public QTSuperclass(Path<? extends Superclass> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QTSuperclass(PathMetadata metadata) {
        super(Superclass.class, metadata);
    }

}

