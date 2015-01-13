package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.DateTimePath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SDocument2 is a Querydsl querydsl type for SDocument2
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SDocument2 extends com.querydsl.sql.RelationalPathBase<SDocument2> {

    private static final long serialVersionUID = 438426745;

    public static final SDocument2 document2_ = new SDocument2("document2_");

    public final NumberPath<Double> createdBy = createNumber("createdBy", Double.class);

    public final DateTimePath<java.sql.Timestamp> creationDate = createDateTime("creationDate", java.sql.Timestamp.class);

    public final NumberPath<Double> deletedBy = createNumber("deletedBy", Double.class);

    public final DateTimePath<java.sql.Timestamp> deletedDate = createDateTime("deletedDate", java.sql.Timestamp.class);

    public final StringPath documentBody = createString("documentBody");

    public final NumberPath<Double> documentStatus = createNumber("documentStatus", Double.class);

    public final StringPath documentSummary = createString("documentSummary");

    public final StringPath documentTitle = createString("documentTitle");

    public final NumberPath<Double> documentVersion = createNumber("documentVersion", Double.class);

    public final NumberPath<Double> entryId = createNumber("entryId", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.sql.Timestamp> modificationDate = createDateTime("modificationDate", java.sql.Timestamp.class);

    public final NumberPath<Double> modifiedBy = createNumber("modifiedBy", Double.class);

    public final com.querydsl.sql.PrimaryKey<SDocument2> primary = createPrimaryKey(id);

    public SDocument2(String variable) {
        super(SDocument2.class, forVariable(variable), "", "document2_");
        addMetadata();
    }

    public SDocument2(String variable, String schema, String table) {
        super(SDocument2.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SDocument2(Path<? extends SDocument2> path) {
        super(path.getType(), path.getMetadata(), "", "document2_");
        addMetadata();
    }

    public SDocument2(PathMetadata<?> metadata) {
        super(SDocument2.class, metadata, "", "document2_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdBy, ColumnMetadata.named("createdBy").withIndex(2).ofType(8).withSize(22).notNull());
        addMetadata(creationDate, ColumnMetadata.named("creationDate").withIndex(3).ofType(93).withSize(19));
        addMetadata(deletedBy, ColumnMetadata.named("deletedBy").withIndex(4).ofType(8).withSize(22).notNull());
        addMetadata(deletedDate, ColumnMetadata.named("deletedDate").withIndex(5).ofType(93).withSize(19));
        addMetadata(documentBody, ColumnMetadata.named("documentBody").withIndex(6).ofType(12).withSize(255));
        addMetadata(documentStatus, ColumnMetadata.named("documentStatus").withIndex(7).ofType(8).withSize(22).notNull());
        addMetadata(documentSummary, ColumnMetadata.named("documentSummary").withIndex(8).ofType(12).withSize(255));
        addMetadata(documentTitle, ColumnMetadata.named("documentTitle").withIndex(9).ofType(12).withSize(255));
        addMetadata(documentVersion, ColumnMetadata.named("documentVersion").withIndex(10).ofType(8).withSize(22).notNull());
        addMetadata(entryId, ColumnMetadata.named("entryId").withIndex(11).ofType(8).withSize(22).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(modificationDate, ColumnMetadata.named("modificationDate").withIndex(12).ofType(93).withSize(19));
        addMetadata(modifiedBy, ColumnMetadata.named("modifiedBy").withIndex(13).ofType(8).withSize(22).notNull());
    }

}

