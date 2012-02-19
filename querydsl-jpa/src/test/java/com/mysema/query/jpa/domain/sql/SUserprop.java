package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SUserprop is a Querydsl query type for SUserprop
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SUserprop extends com.mysema.query.sql.RelationalPathBase<SUserprop> {

    private static final long serialVersionUID = 356895135;

    public static final SUserprop userprop = new SUserprop("USERPROP_");

    public final StringPath categorydescription = createString("CATEGORYDESCRIPTION");

    public final StringPath categoryname = createString("CATEGORYNAME");

    public final NumberPath<Double> createdby = createNumber("CREATEDBY", Double.class);

    public final DateTimePath<java.sql.Timestamp> creationdate = createDateTime("CREATIONDATE", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> deletedate = createDateTime("DELETEDATE", java.sql.Timestamp.class);

    public final NumberPath<Double> deletedby = createNumber("DELETEDBY", Double.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final DateTimePath<java.sql.Timestamp> modificationdate = createDateTime("MODIFICATIONDATE", java.sql.Timestamp.class);

    public final NumberPath<Double> modifiedby = createNumber("MODIFIEDBY", Double.class);

    public final com.mysema.query.sql.PrimaryKey<SUserprop> sql120219232331240 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SUserprop_category> _fk851f48d3904c19df = createInvForeignKey(id, "USERPROP__ID");

    public final com.mysema.query.sql.ForeignKey<SUserprop_categoryprop> _fke0fdb7d0904c19df = createInvForeignKey(id, "USERPROP__ID");

    public final com.mysema.query.sql.ForeignKey<SUser2_userprop> _fk4611b46aa56541dd = createInvForeignKey(id, "PROPERTIES_ID");

    public SUserprop(String variable) {
        super(SUserprop.class, forVariable(variable), "APP", "USERPROP_");
    }

    public SUserprop(Path<? extends SUserprop> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "USERPROP_");
    }

    public SUserprop(PathMetadata<?> metadata) {
        super(SUserprop.class, metadata, "APP", "USERPROP_");
    }

}

