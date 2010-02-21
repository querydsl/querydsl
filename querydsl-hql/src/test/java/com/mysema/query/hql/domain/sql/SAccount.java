package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * SAccount is a Querydsl query type for SAccount
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="ACCOUNT")
public class SAccount extends PEntity<SAccount> {

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PNumber<Long> ownerI = createNumber("OWNER_I", Long.class);

    public final PString somedata = createString("SOMEDATA");

    public SAccount(String variable) {
        super(SAccount.class, forVariable(variable));
    }

    public SAccount(PEntity<? extends SAccount> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SAccount(PathMetadata<?> metadata) {
        super(SAccount.class, metadata);
    }

}

