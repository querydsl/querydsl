package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.StringPath;


/**
 * SSysstatements is a Querydsl query type for SSysstatements
 */
public class SSysstatements extends com.mysema.query.sql.RelationalPathBase<SSysstatements> {

    private static final long serialVersionUID = 1278778912;

    public static final SSysstatements sysstatements = new SSysstatements("SYSSTATEMENTS");

    public final StringPath compilationschemaid = createString("COMPILATIONSCHEMAID");

    public final DateTimePath<java.sql.Timestamp> lastcompiled = createDateTime("LASTCOMPILED", java.sql.Timestamp.class);

    public final StringPath schemaid = createString("SCHEMAID");

    public final StringPath stmtid = createString("STMTID");

    public final StringPath stmtname = createString("STMTNAME");

    public final StringPath text = createString("TEXT");

    public final StringPath type = createString("TYPE");

    public final StringPath usingtext = createString("USINGTEXT");

    public final BooleanPath valid = createBoolean("VALID");

    public SSysstatements(String variable) {
        super(SSysstatements.class, forVariable(variable), "SYS", "SYSSTATEMENTS");
    }

    public SSysstatements(Path<? extends SSysstatements> entity) {
        super(entity.getType(), entity.getMetadata(), "SYS", "SYSSTATEMENTS");
    }

    public SSysstatements(PathMetadata<?> metadata) {
        super(SSysstatements.class, metadata, "SYS", "SYSSTATEMENTS");
    }

}

