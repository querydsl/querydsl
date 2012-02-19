package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SDocument is a Querydsl query type for SDocument
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SDocument extends com.mysema.query.sql.RelationalPathBase<SDocument> {

    private static final long serialVersionUID = 1484253452;

    public static final SDocument document = new SDocument("DOCUMENT_");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final DatePath<java.sql.Date> validto = createDate("VALIDTO", java.sql.Date.class);

    public final com.mysema.query.sql.PrimaryKey<SDocument> sql120219232323580 = createPrimaryKey(id);

    public SDocument(String variable) {
        super(SDocument.class, forVariable(variable), "APP", "DOCUMENT_");
    }

    public SDocument(Path<? extends SDocument> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "DOCUMENT_");
    }

    public SDocument(PathMetadata<?> metadata) {
        super(SDocument.class, metadata, "APP", "DOCUMENT_");
    }

}

