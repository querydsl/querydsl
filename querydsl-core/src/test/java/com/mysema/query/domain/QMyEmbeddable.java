package com.mysema.query.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMyEmbeddable is a Querydsl query type for MyEmbeddable
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QMyEmbeddable extends BeanPath<MyEmbeddable> {

    private static final long serialVersionUID = -968265626;

    public static final QMyEmbeddable myEmbeddable = new QMyEmbeddable("myEmbeddable");

    public final NumberPath<Integer> foo = createNumber("foo", Integer.class);

    public QMyEmbeddable(String variable) {
        super(MyEmbeddable.class, forVariable(variable));
    }

    public QMyEmbeddable(BeanPath<? extends MyEmbeddable> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QMyEmbeddable(PathMetadata<?> metadata) {
        super(MyEmbeddable.class, metadata);
    }

}
