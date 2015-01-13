package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SUserprop_categoryprop is a Querydsl querydsl type for SUserprop_categoryprop
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SUserprop_categoryprop extends com.querydsl.sql.RelationalPathBase<SUserprop_categoryprop> {

    private static final long serialVersionUID = -190294914;

    public static final SUserprop_categoryprop userprop_categoryprop_ = new SUserprop_categoryprop("userprop__categoryprop_");

    public final NumberPath<Long> propertiesId = createNumber("propertiesId", Long.class);

    public final NumberPath<Long> userprop_id = createNumber("userprop_id", Long.class);

    public final com.querydsl.sql.PrimaryKey<SUserprop_categoryprop> primary = createPrimaryKey(propertiesId, userprop_id);

    public final com.querydsl.sql.ForeignKey<SCategoryprop> fke0fdb7d0fd94cd90 = createForeignKey(propertiesId, "id");

    public final com.querydsl.sql.ForeignKey<SUserprop> fke0fdb7d0904c19df = createForeignKey(userprop_id, "id");

    public SUserprop_categoryprop(String variable) {
        super(SUserprop_categoryprop.class, forVariable(variable), "", "userprop__categoryprop_");
        addMetadata();
    }

    public SUserprop_categoryprop(String variable, String schema, String table) {
        super(SUserprop_categoryprop.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SUserprop_categoryprop(Path<? extends SUserprop_categoryprop> path) {
        super(path.getType(), path.getMetadata(), "", "userprop__categoryprop_");
        addMetadata();
    }

    public SUserprop_categoryprop(PathMetadata<?> metadata) {
        super(SUserprop_categoryprop.class, metadata, "", "userprop__categoryprop_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(propertiesId, ColumnMetadata.named("properties_id").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(userprop_id, ColumnMetadata.named("userprop__id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

