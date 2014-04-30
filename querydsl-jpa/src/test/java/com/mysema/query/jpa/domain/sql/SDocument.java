package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SDocument is a Querydsl query type for SDocument
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SDocument extends com.mysema.query.sql.RelationalPathBase<SDocument> {

    private static final long serialVersionUID = -1232783149;

    public static final SDocument document_ = new SDocument("document_");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final DatePath<java.sql.Date> validTo = createDate("validTo", java.sql.Date.class);

    public final com.mysema.query.sql.PrimaryKey<SDocument> primary = createPrimaryKey(id);

    public SDocument(String variable) {
        super(SDocument.class, forVariable(variable), "null", "document_");
        addMetadata();
    }

    public SDocument(String variable, String schema, String table) {
        super(SDocument.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SDocument(Path<? extends SDocument> path) {
        super(path.getType(), path.getMetadata(), "null", "document_");
        addMetadata();
    }

    public SDocument(PathMetadata<?> metadata) {
        super(SDocument.class, metadata, "null", "document_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
        addMetadata(validTo, ColumnMetadata.named("validTo").withIndex(3).ofType(91).withSize(10));
    }

}

