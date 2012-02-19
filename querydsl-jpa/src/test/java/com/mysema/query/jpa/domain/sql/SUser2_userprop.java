package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SUser2_userprop is a Querydsl query type for SUser2_userprop
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SUser2_userprop extends com.mysema.query.sql.RelationalPathBase<SUser2_userprop> {

    private static final long serialVersionUID = -2097511337;

    public static final SUser2_userprop user2_userprop = new SUser2_userprop("USER2__USERPROP_");

    public final NumberPath<Long> propertiesId = createNumber("PROPERTIES_ID", Long.class);

    public final NumberPath<Long> user2_id = createNumber("USER2__ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SUser2_userprop> sql120219232330760 = createPrimaryKey(propertiesId, user2_id);

    public final com.mysema.query.sql.ForeignKey<SUser2> fk4611b46af21971a1 = createForeignKey(user2_id, "ID");

    public final com.mysema.query.sql.ForeignKey<SUserprop> fk4611b46aa56541dd = createForeignKey(propertiesId, "ID");

    public SUser2_userprop(String variable) {
        super(SUser2_userprop.class, forVariable(variable), "APP", "USER2__USERPROP_");
    }

    public SUser2_userprop(Path<? extends SUser2_userprop> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "USER2__USERPROP_");
    }

    public SUser2_userprop(PathMetadata<?> metadata) {
        super(SUser2_userprop.class, metadata, "APP", "USER2__USERPROP_");
    }

}

