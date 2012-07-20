package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SSequence is a Querydsl query type for SSequence
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SSequence extends com.mysema.query.sql.RelationalPathBase<SSequence> {

    private static final long serialVersionUID = 1972080562;

    public static final SSequence sequence = new SSequence("SEQUENCE");

    public final NumberPath<java.math.BigDecimal> seqCount = createNumber("SEQ_COUNT", java.math.BigDecimal.class);

    public final StringPath seqName = createString("SEQ_NAME");

    public final com.mysema.query.sql.PrimaryKey<SSequence> primary = createPrimaryKey(seqName);

    public SSequence(String variable) {
        super(SSequence.class, forVariable(variable), "null", "SEQUENCE");
    }

    public SSequence(Path<? extends SSequence> path) {
        super(path.getType(), path.getMetadata(), "null", "SEQUENCE");
    }

    public SSequence(PathMetadata<?> metadata) {
        super(SSequence.class, metadata, "null", "SEQUENCE");
    }

}

