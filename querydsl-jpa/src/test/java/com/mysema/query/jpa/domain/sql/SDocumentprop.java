package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SDocumentprop is a Querydsl query type for SDocumentprop
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SDocumentprop extends com.mysema.query.sql.RelationalPathBase<SDocumentprop> {

    private static final long serialVersionUID = 233547728;

    public static final SDocumentprop documentprop_ = new SDocumentprop("documentprop_");

    public final NumberPath<Double> documentId = createNumber("documentId", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath propName = createString("propName");

    public final StringPath propValue = createString("propValue");

    public final StringPath propValueDetails = createString("propValueDetails");

    public final com.mysema.query.sql.PrimaryKey<SDocumentprop> primary = createPrimaryKey(id);

    public SDocumentprop(String variable) {
        super(SDocumentprop.class, forVariable(variable), "null", "documentprop_");
        addMetadata();
    }

    public SDocumentprop(String variable, String schema, String table) {
        super(SDocumentprop.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SDocumentprop(Path<? extends SDocumentprop> path) {
        super(path.getType(), path.getMetadata(), "null", "documentprop_");
        addMetadata();
    }

    public SDocumentprop(PathMetadata<?> metadata) {
        super(SDocumentprop.class, metadata, "null", "documentprop_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(documentId, ColumnMetadata.named("documentId").withIndex(2).ofType(8).withSize(22).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(propName, ColumnMetadata.named("propName").withIndex(3).ofType(12).withSize(255));
        addMetadata(propValue, ColumnMetadata.named("propValue").withIndex(4).ofType(12).withSize(255));
        addMetadata(propValueDetails, ColumnMetadata.named("propValueDetails").withIndex(5).ofType(12).withSize(255));
    }

}

