package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.DatePath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SDocument is a Querydsl querydsl type for SDocument
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SDocument extends com.querydsl.sql.RelationalPathBase<SDocument> {

    private static final long serialVersionUID = -1232783149;

    public static final SDocument document_ = new SDocument("document_");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final DatePath<java.sql.Date> validTo = createDate("validTo", java.sql.Date.class);

    public final com.querydsl.sql.PrimaryKey<SDocument> primary = createPrimaryKey(id);

    public SDocument(String variable) {
        super(SDocument.class, forVariable(variable), "", "document_");
        addMetadata();
    }

    public SDocument(String variable, String schema, String table) {
        super(SDocument.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SDocument(Path<? extends SDocument> path) {
        super(path.getType(), path.getMetadata(), "", "document_");
        addMetadata();
    }

    public SDocument(PathMetadata<?> metadata) {
        super(SDocument.class, metadata, "", "document_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
        addMetadata(validTo, ColumnMetadata.named("validTo").withIndex(3).ofType(91).withSize(10));
    }

}

