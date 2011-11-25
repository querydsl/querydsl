package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SDocument is a Querydsl query type for SDocument
 */
public class SDocument extends RelationalPathBase<SDocument> {

    private static final long serialVersionUID = 1919248740;

    public static final SDocument document = new SDocument("DOCUMENT");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final DatePath<java.sql.Date> validto = createDate("VALIDTO", java.sql.Date.class);

    public final PrimaryKey<SDocument> sql100819184432950 = createPrimaryKey(id);

    public SDocument(String variable) {
        super(SDocument.class, forVariable(variable), null, "DOCUMENT");
    }

    public SDocument(BeanPath<? extends SDocument> entity) {
        super(entity.getType(), entity.getMetadata(), null, "DOCUMENT");
    }

    public SDocument(PathMetadata<?> metadata) {
        super(SDocument.class, metadata, null, "DOCUMENT");
    }

}

