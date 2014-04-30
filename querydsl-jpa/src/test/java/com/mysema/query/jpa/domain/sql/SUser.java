package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SUser is a Querydsl query type for SUser
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SUser extends com.mysema.query.sql.RelationalPathBase<SUser> {

    private static final long serialVersionUID = -109124701;

    public static final SUser user_ = new SUser("user_");

    public final NumberPath<Integer> companyId = createNumber("companyId", Integer.class);

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lastName = createString("lastName");

    public final StringPath userName = createString("userName");

    public final com.mysema.query.sql.PrimaryKey<SUser> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCompany> fk6a68df4dc953998 = createForeignKey(companyId, "id");

    public final com.mysema.query.sql.ForeignKey<SEmployee> _fk9d39ef712743b59c = createInvForeignKey(id, "user_id");

    public SUser(String variable) {
        super(SUser.class, forVariable(variable), "null", "user_");
        addMetadata();
    }

    public SUser(String variable, String schema, String table) {
        super(SUser.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SUser(Path<? extends SUser> path) {
        super(path.getType(), path.getMetadata(), "null", "user_");
        addMetadata();
    }

    public SUser(PathMetadata<?> metadata) {
        super(SUser.class, metadata, "null", "user_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(companyId, ColumnMetadata.named("company_id").withIndex(5).ofType(4).withSize(10));
        addMetadata(firstName, ColumnMetadata.named("firstName").withIndex(2).ofType(12).withSize(255));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(lastName, ColumnMetadata.named("lastName").withIndex(3).ofType(12).withSize(255));
        addMetadata(userName, ColumnMetadata.named("userName").withIndex(4).ofType(12).withSize(255));
    }

}

