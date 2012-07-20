package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SStatement is a Querydsl query type for SStatement
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SStatement extends com.mysema.query.sql.RelationalPathBase<SStatement> {

    private static final long serialVersionUID = 33492894;

    public static final SStatement statement = new SStatement("statement");

    public final NumberPath<Long> model = createNumber("model", Long.class);

    public final NumberPath<Long> object = createNumber("object", Long.class);

    public final NumberPath<Long> predicate = createNumber("predicate", Long.class);

    public final NumberPath<Long> subject = createNumber("subject", Long.class);

    public SStatement(String variable) {
        super(SStatement.class, forVariable(variable), "null", "statement");
    }

    public SStatement(Path<? extends SStatement> path) {
        super(path.getType(), path.getMetadata(), "null", "statement");
    }

    public SStatement(PathMetadata<?> metadata) {
        super(SStatement.class, metadata, "null", "statement");
    }

}

