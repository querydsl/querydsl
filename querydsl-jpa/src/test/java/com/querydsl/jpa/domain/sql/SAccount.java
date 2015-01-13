package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SAccount is a Querydsl querydsl type for SAccount
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SAccount extends com.querydsl.sql.RelationalPathBase<SAccount> {

    private static final long serialVersionUID = -1514613821;

    public static final SAccount account_ = new SAccount("account_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> ownerI = createNumber("ownerI", Long.class);

    public final StringPath someData = createString("someData");

    public final com.querydsl.sql.PrimaryKey<SAccount> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SPerson> fk809dbbd28cfac74 = createForeignKey(ownerI, "i");

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

