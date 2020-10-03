package com.querydsl.core.domain.query3;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import com.querydsl.core.annotations.Generated;
import com.querydsl.core.domain.IdNamePair;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.core.types.dsl.StringPath;


/**
 * QIdNamePair is a Querydsl query type for IdNamePair
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QTIdNamePair extends BeanPath<IdNamePair<?>> {

    private static final long serialVersionUID = -1491444395;

    public static final QTIdNamePair idNamePair = new QTIdNamePair("idNamePair");

    public final StringPath id = createString("id");

    public final SimplePath<Object> name = createSimple("name", Object.class);

    @SuppressWarnings("unchecked")
    public QTIdNamePair(String variable) {
        super((Class) IdNamePair.class, forVariable(variable));
    }

    public QTIdNamePair(Path<? extends IdNamePair<?>> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    @SuppressWarnings("unchecked")
    public QTIdNamePair(PathMetadata metadata) {
        super((Class) IdNamePair.class, metadata);
    }

}

