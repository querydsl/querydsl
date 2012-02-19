package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SUserprop_categoryprop is a Querydsl query type for SUserprop_categoryprop
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SUserprop_categoryprop extends com.mysema.query.sql.RelationalPathBase<SUserprop_categoryprop> {

    private static final long serialVersionUID = 1794976769;

    public static final SUserprop_categoryprop userprop_categoryprop = new SUserprop_categoryprop("USERPROP__CATEGORYPROP_");

    public final NumberPath<Long> propertiesId = createNumber("PROPERTIES_ID", Long.class);

    public final NumberPath<Long> userprop_id = createNumber("USERPROP__ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SUserprop_categoryprop> sql120219232331810 = createPrimaryKey(propertiesId, userprop_id);

    public final com.mysema.query.sql.ForeignKey<SCategoryprop> fke0fdb7d0fd94cd90 = createForeignKey(propertiesId, "ID");

    public final com.mysema.query.sql.ForeignKey<SUserprop> fke0fdb7d0904c19df = createForeignKey(userprop_id, "ID");

    public SUserprop_categoryprop(String variable) {
        super(SUserprop_categoryprop.class, forVariable(variable), "APP", "USERPROP__CATEGORYPROP_");
    }

    public SUserprop_categoryprop(Path<? extends SUserprop_categoryprop> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "USERPROP__CATEGORYPROP_");
    }

    public SUserprop_categoryprop(PathMetadata<?> metadata) {
        super(SUserprop_categoryprop.class, metadata, "APP", "USERPROP__CATEGORYPROP_");
    }

}

