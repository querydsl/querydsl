package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SUser is a Querydsl query type for SUser
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SUser extends com.mysema.query.sql.RelationalPathBase<SUser> {

    private static final long serialVersionUID = -1388993476;

    public static final SUser user = new SUser("user_");

    public final NumberPath<Integer> companyId = createNumber("company_id", Integer.class);

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lastName = createString("lastName");

    public final StringPath userName = createString("userName");

    public final com.mysema.query.sql.PrimaryKey<SUser> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCompany> user_COMPANYIDFK = createForeignKey(companyId, "id");

    public final com.mysema.query.sql.ForeignKey<SCompany> fk6a68df4dc953998 = createForeignKey(companyId, "id");

    public final com.mysema.query.sql.ForeignKey<SEmployee> _employee_USERIDFK = createInvForeignKey(id, "user_id");

    public final com.mysema.query.sql.ForeignKey<SEmployee> _fk9d39ef712743b59c = createInvForeignKey(id, "user_id");

    public SUser(String variable) {
        super(SUser.class, forVariable(variable), "null", "user_");
    }

    public SUser(Path<? extends SUser> path) {
        super(path.getType(), path.getMetadata(), "null", "user_");
    }

    public SUser(PathMetadata<?> metadata) {
        super(SUser.class, metadata, "null", "user_");
    }

}

