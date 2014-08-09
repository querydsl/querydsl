package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SAccount is a Querydsl query type for SAccount
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SAccount extends com.mysema.query.sql.RelationalPathBase<SAccount> {

    private static final long serialVersionUID = -1514613821;

    public static final SAccount account_ = new SAccount("account_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> ownerI = createNumber("ownerI", Long.class);

    public final StringPath someData = createString("someData");

    public final com.mysema.query.sql.PrimaryKey<SAccount> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SPerson> fk809dbbd28cfac74 = createForeignKey(ownerI, "i");

    public SAccount(String variable) {
        super(SAccount.class, forVariable(variable), "", "account_");
        addMetadata();
    }

    public SAccount(String variable, String schema, String table) {
        super(SAccount.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SAccount(Path<? extends SAccount> path) {
        super(path.getType(), path.getMetadata(), "", "account_");
        addMetadata();
    }

    public SAccount(PathMetadata<?> metadata) {
        super(SAccount.class, metadata, "", "account_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(ownerI, ColumnMetadata.named("owner_i").withIndex(3).ofType(-5).withSize(19));
        addMetadata(someData, ColumnMetadata.named("someData").withIndex(2).ofType(12).withSize(255));
    }

}

