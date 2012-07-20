package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SDocument is a Querydsl query type for SDocument
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SDocument extends com.mysema.query.sql.RelationalPathBase<SDocument> {

    private static final long serialVersionUID = 1484253452;

    public static final SDocument document = new SDocument("document_");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final DatePath<java.sql.Date> validto = createDate("VALIDTO", java.sql.Date.class);

    public final com.mysema.query.sql.PrimaryKey<SDocument> primary = createPrimaryKey(id);

    public SDocument(String variable) {
        super(SDocument.class, forVariable(variable), "null", "document_");
    }

    public SDocument(Path<? extends SDocument> path) {
        super(path.getType(), path.getMetadata(), "null", "document_");
    }

    public SDocument(PathMetadata<?> metadata) {
        super(SDocument.class, metadata, "null", "document_");
    }

}

