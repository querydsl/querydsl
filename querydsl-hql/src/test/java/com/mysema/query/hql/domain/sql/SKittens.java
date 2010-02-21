package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SKittens is a Querydsl query type for SKittens
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="KITTENS")
public class SKittens extends PEntity<SKittens> {

    public final PNumber<Integer> animalId = createNumber("ANIMAL_ID", Integer.class);

    public final PNumber<Integer> ind = createNumber("IND", Integer.class);

    public final PNumber<Integer> kittensId = createNumber("KITTENS_ID", Integer.class);

    public SKittens(String variable) {
        super(SKittens.class, forVariable(variable));
    }

    public SKittens(PEntity<? extends SKittens> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SKittens(PathMetadata<?> metadata) {
        super(SKittens.class, metadata);
    }

}

