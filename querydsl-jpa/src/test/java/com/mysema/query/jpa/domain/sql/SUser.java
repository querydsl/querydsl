package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SUser is a Querydsl query type for SUser
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SUser extends com.mysema.query.sql.RelationalPathBase<SUser> {

    private static final long serialVersionUID = -1388993476;

    public static final SUser user = new SUser("USER_");

    public final NumberPath<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final StringPath firstname = createString("FIRSTNAME");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath lastname = createString("LASTNAME");

    public final StringPath username = createString("USERNAME");

    public final com.mysema.query.sql.PrimaryKey<SUser> sql120219232331040 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCompany> fk6a68df4dc953998 = createForeignKey(companyId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEmployee> _fk9d39ef712743b59c = createInvForeignKey(id, "USER_ID");

    public SUser(String variable) {
        super(SUser.class, forVariable(variable), "APP", "USER_");
    }

    public SUser(Path<? extends SUser> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "USER_");
    }

    public SUser(PathMetadata<?> metadata) {
        super(SUser.class, metadata, "APP", "USER_");
    }

}

