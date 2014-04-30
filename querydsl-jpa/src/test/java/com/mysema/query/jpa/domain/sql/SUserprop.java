package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SUserprop is a Querydsl query type for SUserprop
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SUserprop extends com.mysema.query.sql.RelationalPathBase<SUserprop> {

    private static final long serialVersionUID = -1821152608;

    public static final SUserprop userprop_ = new SUserprop("userprop_");

    public final StringPath categoryDescription = createString("categoryDescription");

    public final StringPath categoryName = createString("categoryName");

    public final NumberPath<Double> createdBy = createNumber("createdBy", Double.class);

    public final DateTimePath<java.sql.Timestamp> creationDate = createDateTime("creationDate", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> deleteDate = createDateTime("deleteDate", java.sql.Timestamp.class);

    public final NumberPath<Double> deletedBy = createNumber("deletedBy", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.sql.Timestamp> modificationDate = createDateTime("modificationDate", java.sql.Timestamp.class);

    public final NumberPath<Double> modifiedBy = createNumber("modifiedBy", Double.class);

    public final com.mysema.query.sql.PrimaryKey<SUserprop> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SUserprop_category> _fk851f48d3904c19df = createInvForeignKey(id, "userprop__id");

    public final com.mysema.query.sql.ForeignKey<SUserprop_categoryprop> _fke0fdb7d0904c19df = createInvForeignKey(id, "userprop__id");

    public final com.mysema.query.sql.ForeignKey<SUser2_userprop> _fk4611b46aa56541dd = createInvForeignKey(id, "properties_id");

    public SUserprop(String variable) {
        super(SUserprop.class, forVariable(variable), "null", "userprop_");
        addMetadata();
    }

    public SUserprop(String variable, String schema, String table) {
        super(SUserprop.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SUserprop(Path<? extends SUserprop> path) {
        super(path.getType(), path.getMetadata(), "null", "userprop_");
        addMetadata();
    }

    public SUserprop(PathMetadata<?> metadata) {
        super(SUserprop.class, metadata, "null", "userprop_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(categoryDescription, ColumnMetadata.named("categoryDescription").withIndex(2).ofType(12).withSize(255));
        addMetadata(categoryName, ColumnMetadata.named("categoryName").withIndex(3).ofType(12).withSize(255));
        addMetadata(createdBy, ColumnMetadata.named("createdBy").withIndex(4).ofType(8).withSize(22).notNull());
        addMetadata(creationDate, ColumnMetadata.named("creationDate").withIndex(5).ofType(93).withSize(19));
        addMetadata(deleteDate, ColumnMetadata.named("deleteDate").withIndex(6).ofType(93).withSize(19));
        addMetadata(deletedBy, ColumnMetadata.named("deletedBy").withIndex(7).ofType(8).withSize(22).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(modificationDate, ColumnMetadata.named("modificationDate").withIndex(8).ofType(93).withSize(19));
        addMetadata(modifiedBy, ColumnMetadata.named("modifiedBy").withIndex(9).ofType(8).withSize(22).notNull());
    }

}

