package com.querydsl.core.domain.query;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.domain.IdNamePair;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.BeanPath;
import com.querydsl.core.types.path.SimplePath;
import com.querydsl.core.types.path.StringPath;


/**
 * QIdNamePair is a Querydsl query type for IdNamePair
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QIdNamePair extends BeanPath<IdNamePair<?>> {

    private static final long serialVersionUID = -1491444395;

    public static final QIdNamePair idNamePair = new QIdNamePair("idNamePair");

    public final StringPath id = createString("id");

    public final SimplePath<Object> name = createSimple("name", Object.class);

    @SuppressWarnings("unchecked")
    public QIdNamePair(String variable) {
        super((Class)IdNamePair.class, forVariable(variable));
    }

    public QIdNamePair(Path<? extends IdNamePair<?>> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    @SuppressWarnings("unchecked")
    public QIdNamePair(PathMetadata<?> metadata) {
        super((Class)IdNamePair.class, metadata);
    }

}

