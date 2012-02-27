package com.mysema.query.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QIdNamePair is a Querydsl query type for IdNamePair
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
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

