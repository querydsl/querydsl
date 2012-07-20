package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SUserprop is a Querydsl query type for SUserprop
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SUserprop extends com.mysema.query.sql.RelationalPathBase<SUserprop> {

    private static final long serialVersionUID = 356895135;

    public static final SUserprop userprop = new SUserprop("userprop_");

    public final StringPath categorydescription = createString("CATEGORYDESCRIPTION");

    public final StringPath categoryname = createString("CATEGORYNAME");

    public final NumberPath<Double> createdby = createNumber("CREATEDBY", Double.class);

    public final DateTimePath<java.sql.Timestamp> creationdate = createDateTime("CREATIONDATE", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> deletedate = createDateTime("DELETEDATE", java.sql.Timestamp.class);

    public final NumberPath<Double> deletedby = createNumber("DELETEDBY", Double.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final DateTimePath<java.sql.Timestamp> modificationdate = createDateTime("MODIFICATIONDATE", java.sql.Timestamp.class);

    public final NumberPath<Double> modifiedby = createNumber("MODIFIEDBY", Double.class);

    public final com.mysema.query.sql.PrimaryKey<SUserprop> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SUserprop_category> _userprop_category_UserPropIDFK = createInvForeignKey(id, "UserProp_ID");

    public final com.mysema.query.sql.ForeignKey<SUser2_userprop> _user2_userprop_propertiesIDFK = createInvForeignKey(id, "properties_ID");

    public final com.mysema.query.sql.ForeignKey<SUserprop_category> _fk851f48d3904c19df = createInvForeignKey(id, "userprop__id");

    public final com.mysema.query.sql.ForeignKey<SUserprop_categoryprop> _fke0fdb7d0904c19df = createInvForeignKey(id, "userprop__id");

    public final com.mysema.query.sql.ForeignKey<SUserprop_categoryprop> _userprop_categoryprop_UserPropIDFK = createInvForeignKey(id, "UserProp_ID");

    public SUserprop(String variable) {
        super(SUserprop.class, forVariable(variable), "null", "userprop_");
    }

    public SUserprop(Path<? extends SUserprop> path) {
        super(path.getType(), path.getMetadata(), "null", "userprop_");
    }

    public SUserprop(PathMetadata<?> metadata) {
        super(SUserprop.class, metadata, "null", "userprop_");
    }

}

