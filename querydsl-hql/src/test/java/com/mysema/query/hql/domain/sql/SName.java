package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SName is a Querydsl query type for SName
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="NAME")
public class SName extends PEntity<SName> {

    public final PString firstname = createString("FIRSTNAME");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PString lastname = createString("LASTNAME");

    public final PString nickname = createString("NICKNAME");

    public SName(String variable) {
        super(SName.class, forVariable(variable));
    }

    public SName(PEntity<? extends SName> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SName(PathMetadata<?> metadata) {
        super(SName.class, metadata);
    }

}

