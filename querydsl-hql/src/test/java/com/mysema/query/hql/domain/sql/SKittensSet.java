package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SKittensSet is a Querydsl query type for SKittensSet
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="KITTENS_SET")
public class SKittensSet extends PEntity<SKittensSet> {

    public final PNumber<Integer> animalId = createNumber("ANIMAL_ID", Integer.class);

    public final PNumber<Integer> kittenssetId = createNumber("KITTENSSET_ID", Integer.class);

    public SKittensSet(String variable) {
        super(SKittensSet.class, forVariable(variable));
    }

    public SKittensSet(PEntity<? extends SKittensSet> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SKittensSet(PathMetadata<?> metadata) {
        super(SKittensSet.class, metadata);
    }

}

