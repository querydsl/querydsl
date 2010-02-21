package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;
import com.mysema.query.types.expr.*;
import com.mysema.query.types.custom.*;

/**
 * SDocument is a Querydsl query type for SDocument
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="DOCUMENT")
public class SDocument extends PEntity<SDocument> {

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString name = createString("NAME");

    public final PComparable<java.util.Date> validto = createComparable("VALIDTO", java.util.Date.class);

    public SDocument(String variable) {
        super(SDocument.class, forVariable(variable));
    }

    public SDocument(PEntity<? extends SDocument> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SDocument(PathMetadata<?> metadata) {
        super(SDocument.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

