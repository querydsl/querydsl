package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SPersonid is a Querydsl query type for SPersonid
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="PERSONID")
public class SPersonid extends PEntity<SPersonid> {

    public final PString country = createString("COUNTRY");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Integer> medicarenumber = createNumber("MEDICARENUMBER", Integer.class);

    public SPersonid(String variable) {
        super(SPersonid.class, forVariable(variable));
    }

    public SPersonid(PEntity<? extends SPersonid> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SPersonid(PathMetadata<?> metadata) {
        super(SPersonid.class, metadata);
    }

}

