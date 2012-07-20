package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SAccount is a Querydsl query type for SAccount
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SAccount extends com.mysema.query.sql.RelationalPathBase<SAccount> {

    private static final long serialVersionUID = 1475162140;

    public static final SAccount account = new SAccount("account_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Long> ownerI = createNumber("OWNER_I", Long.class);

    public final StringPath somedata = createString("SOMEDATA");

    public final com.mysema.query.sql.PrimaryKey<SAccount> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SPerson> account_OWNERIFK = createForeignKey(ownerI, "I");

    public SAccount(String variable) {
        super(SAccount.class, forVariable(variable), "null", "account_");
    }

    public SAccount(Path<? extends SAccount> path) {
        super(path.getType(), path.getMetadata(), "null", "account_");
    }

    public SAccount(PathMetadata<?> metadata) {
        super(SAccount.class, metadata, "null", "account_");
    }

}

