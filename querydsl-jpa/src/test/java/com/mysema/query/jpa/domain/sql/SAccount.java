package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SAccount is a Querydsl query type for SAccount
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SAccount extends com.mysema.query.sql.RelationalPathBase<SAccount> {

    private static final long serialVersionUID = 1475162140;

    public static final SAccount account = new SAccount("ACCOUNT_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Long> ownerI = createNumber("OWNER_I", Long.class);

    public final StringPath somedata = createString("SOMEDATA");

    public final com.mysema.query.sql.PrimaryKey<SAccount> sql120219232319230 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SPerson> fk809dbbd28cfac74 = createForeignKey(ownerI, "I");

    public SAccount(String variable) {
        super(SAccount.class, forVariable(variable), "APP", "ACCOUNT_");
    }

    public SAccount(Path<? extends SAccount> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "ACCOUNT_");
    }

    public SAccount(PathMetadata<?> metadata) {
        super(SAccount.class, metadata, "APP", "ACCOUNT_");
    }

}

