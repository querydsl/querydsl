package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SUser2 is a Querydsl query type for SUser2
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SUser2 extends com.mysema.query.sql.RelationalPathBase<SUser2> {

    private static final long serialVersionUID = -109124746;

    public static final SUser2 user2 = new SUser2("user2_");

    public final NumberPath<Double> createdby = createNumber("CREATEDBY", Double.class);

    public final DateTimePath<java.sql.Timestamp> creationdate = createDateTime("CREATIONDATE", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> deletedate = createDateTime("DELETEDATE", java.sql.Timestamp.class);

    public final NumberPath<Double> deletedby = createNumber("DELETEDBY", Double.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final DateTimePath<java.sql.Timestamp> modificationdate = createDateTime("MODIFICATIONDATE", java.sql.Timestamp.class);

    public final NumberPath<Double> modifiedby = createNumber("MODIFIEDBY", Double.class);

    public final StringPath useremail = createString("USEREMAIL");

    public final StringPath userfirstname = createString("USERFIRSTNAME");

    public final StringPath userlastname = createString("USERLASTNAME");

    public final StringPath username = createString("USERNAME");

    public final StringPath userpassword = createString("USERPASSWORD");

    public final com.mysema.query.sql.PrimaryKey<SUser2> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SUser2_userprop> _fk4611b46af21971a1 = createInvForeignKey(id, "user2__id");

    public final com.mysema.query.sql.ForeignKey<SUser2_userprop> _user2_userprop_User2IDFK = createInvForeignKey(id, "User2_ID");

    public SUser2(String variable) {
        super(SUser2.class, forVariable(variable), "null", "user2_");
    }

    public SUser2(Path<? extends SUser2> path) {
        super(path.getType(), path.getMetadata(), "null", "user2_");
    }

    public SUser2(PathMetadata<?> metadata) {
        super(SUser2.class, metadata, "null", "user2_");
    }

}

