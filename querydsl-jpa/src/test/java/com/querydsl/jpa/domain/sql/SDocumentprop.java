package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SDocumentprop is a Querydsl querydsl type for SDocumentprop
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SDocumentprop extends com.querydsl.sql.RelationalPathBase<SDocumentprop> {

    private static final long serialVersionUID = 233547728;

    public static final SDocumentprop documentprop_ = new SDocumentprop("documentprop_");

    public final NumberPath<Double> documentId = createNumber("documentId", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath propName = createString("propName");

    public final StringPath propValue = createString("propValue");

    public final StringPath propValueDetails = createString("propValueDetails");

    public final com.querydsl.sql.PrimaryKey<SDocumentprop> primary = createPrimaryKey(id);

    public SDocumentprop(String variable) {
        super(SDocumentprop.class, forVariable(variable), "", "documentprop_");
        addMetadata();
    }

    public SDocumentprop(String variable, String schema, String table) {
        super(SDocumentprop.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SDocumentprop(Path<? extends SDocumentprop> path) {
        super(path.getType(), path.getMetadata(), "", "documentprop_");
        addMetadata();
    }

    public SDocumentprop(PathMetadata<?> metadata) {
        super(SDocumentprop.class, metadata, "", "documentprop_");
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

