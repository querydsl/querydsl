package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;


/**
 * SSysstatistics is a Querydsl query type for SSysstatistics
 */
public class SSysstatistics extends com.mysema.query.sql.RelationalPathBase<SSysstatistics> {

    private static final long serialVersionUID = 1399278175;

    public static final SSysstatistics sysstatistics = new SSysstatistics("SYSSTATISTICS");

    public final NumberPath<Integer> colcount = createNumber("COLCOUNT", Integer.class);

    public final DateTimePath<java.sql.Timestamp> creationtimestamp = createDateTime("CREATIONTIMESTAMP", java.sql.Timestamp.class);

    public final StringPath referenceid = createString("REFERENCEID");

    public final StringPath statid = createString("STATID");

    public final SimplePath<Object> statistics = createSimple("STATISTICS", Object.class);

    public final StringPath tableid = createString("TABLEID");

    public final StringPath type = createString("TYPE");

    public final BooleanPath valid = createBoolean("VALID");

    public SSysstatistics(String variable) {
        super(SSysstatistics.class, forVariable(variable), "SYS", "SYSSTATISTICS");
    }

    public SSysstatistics(Path<? extends SSysstatistics> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSSTATISTICS");
    }

    public SSysstatistics(PathMetadata<?> metadata) {
        super(SSysstatistics.class, metadata, "SYS", "SYSSTATISTICS");
    }

}

