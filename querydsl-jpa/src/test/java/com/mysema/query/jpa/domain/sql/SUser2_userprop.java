package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SUser2_userprop is a Querydsl query type for SUser2_userprop
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SUser2_userprop extends com.mysema.query.sql.RelationalPathBase<SUser2_userprop> {

    private static final long serialVersionUID = -2097511337;

    public static final SUser2_userprop user2_userprop = new SUser2_userprop("user2__userprop_");

    public final NumberPath<Long> propertiesID = createNumber("properties_ID", Long.class);

    public final NumberPath<Long> user2_id = createNumber("user2__id", Long.class);

    public final NumberPath<Long> user2ID = createNumber("User2_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SUser2_userprop> primary = createPrimaryKey(user2ID, propertiesID);

    public final com.mysema.query.sql.ForeignKey<SUser2> fk4611b46af21971a1 = createForeignKey(user2_id, "ID");

    public final com.mysema.query.sql.ForeignKey<SUserprop> user2_userprop_propertiesIDFK = createForeignKey(propertiesID, "ID");

    public final com.mysema.query.sql.ForeignKey<SUser2> user2_userprop_User2IDFK = createForeignKey(user2ID, "ID");

    public SUser2_userprop(String variable) {
        super(SUser2_userprop.class, forVariable(variable), "null", "user2__userprop_");
    }

    public SUser2_userprop(Path<? extends SUser2_userprop> path) {
        super(path.getType(), path.getMetadata(), "null", "user2__userprop_");
    }

    public SUser2_userprop(PathMetadata<?> metadata) {
        super(SUser2_userprop.class, metadata, "null", "user2__userprop_");
    }

}

