package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SUser is a Querydsl querydsl type for SUser
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SUser extends com.querydsl.sql.RelationalPathBase<SUser> {

    private static final long serialVersionUID = -109124701;

    public static final SUser user_ = new SUser("user_");

    public final NumberPath<Integer> companyId = createNumber("companyId", Integer.class);

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lastName = createString("lastName");

    public final StringPath userName = createString("userName");

    public final com.querydsl.sql.PrimaryKey<SUser> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SCompany> fk6a68df4dc953998 = createForeignKey(companyId, "id");

    public final com.querydsl.sql.ForeignKey<SEmployee> _fk9d39ef712743b59c = createInvForeignKey(id, "user_id");

    public SUser(String variable) {
        super(SUser.class, forVariable(variable), "", "user_");
        addMetadata();
    }

    public SUser(String variable, String schema, String table) {
        super(SUser.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SUser(Path<? extends SUser> path) {
        super(path.getType(), path.getMetadata(), "", "user_");
        addMetadata();
    }

    public SUser(PathMetadata<?> metadata) {
        super(SUser.class, metadata, "", "user_");
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

