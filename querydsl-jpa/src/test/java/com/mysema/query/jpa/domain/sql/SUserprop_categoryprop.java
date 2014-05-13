package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SUserprop_categoryprop is a Querydsl query type for SUserprop_categoryprop
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SUserprop_categoryprop extends com.mysema.query.sql.RelationalPathBase<SUserprop_categoryprop> {

    private static final long serialVersionUID = -190294914;

    public static final SUserprop_categoryprop userprop_categoryprop_ = new SUserprop_categoryprop("userprop__categoryprop_");

    public final NumberPath<Long> propertiesId = createNumber("propertiesId", Long.class);

    public final NumberPath<Long> userprop_id = createNumber("userprop_id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SUserprop_categoryprop> primary = createPrimaryKey(propertiesId, userprop_id);

    public final com.mysema.query.sql.ForeignKey<SCategoryprop> fke0fdb7d0fd94cd90 = createForeignKey(propertiesId, "id");

    public final com.mysema.query.sql.ForeignKey<SUserprop> fke0fdb7d0904c19df = createForeignKey(userprop_id, "id");

    public SUserprop_categoryprop(String variable) {
        super(SUserprop_categoryprop.class, forVariable(variable), "null", "userprop__categoryprop_");
        addMetadata();
    }

    public SUserprop_categoryprop(String variable, String schema, String table) {
        super(SUserprop_categoryprop.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SUserprop_categoryprop(Path<? extends SUserprop_categoryprop> path) {
        super(path.getType(), path.getMetadata(), "null", "userprop__categoryprop_");
        addMetadata();
    }

    public SUserprop_categoryprop(PathMetadata<?> metadata) {
        super(SUserprop_categoryprop.class, metadata, "null", "userprop__categoryprop_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(propertiesId, ColumnMetadata.named("properties_id").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(userprop_id, ColumnMetadata.named("userprop__id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

