package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SUserprop_categoryprop is a Querydsl query type for SUserprop_categoryprop
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SUserprop_categoryprop extends com.mysema.query.sql.RelationalPathBase<SUserprop_categoryprop> {

    private static final long serialVersionUID = 1794976769;

    public static final SUserprop_categoryprop userprop_categoryprop = new SUserprop_categoryprop("userprop__categoryprop_");

    public final NumberPath<Long> propertiesID = createNumber("properties_ID", Long.class);

    public final NumberPath<Long> userprop_id = createNumber("userprop__id", Long.class);

    public final NumberPath<Long> userPropID = createNumber("UserProp_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SUserprop_categoryprop> primary = createPrimaryKey(userPropID, propertiesID);

    public final com.mysema.query.sql.ForeignKey<SCategoryprop> userprop_categoryprop_propertiesIDFK = createForeignKey(propertiesID, "ID");

    public final com.mysema.query.sql.ForeignKey<SUserprop> fke0fdb7d0904c19df = createForeignKey(userprop_id, "ID");

    public final com.mysema.query.sql.ForeignKey<SUserprop> userprop_categoryprop_UserPropIDFK = createForeignKey(userPropID, "ID");

    public SUserprop_categoryprop(String variable) {
        super(SUserprop_categoryprop.class, forVariable(variable), "null", "userprop__categoryprop_");
    }

    public SUserprop_categoryprop(Path<? extends SUserprop_categoryprop> path) {
        super(path.getType(), path.getMetadata(), "null", "userprop__categoryprop_");
    }

    public SUserprop_categoryprop(PathMetadata<?> metadata) {
        super(SUserprop_categoryprop.class, metadata, "null", "userprop__categoryprop_");
    }

}

