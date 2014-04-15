package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SLibrary is a Querydsl query type for SLibrary
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SLibrary extends com.mysema.query.sql.RelationalPathBase<SLibrary> {

    private static final long serialVersionUID = 1480035061;

    public static final SLibrary library_ = new SLibrary("library_");

    public final NumberPath<Long> identity = createNumber("identity", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SLibrary> primary = createPrimaryKey(identity);

    public final com.mysema.query.sql.ForeignKey<SBookversion> _fkef4bc070e364cd17 = createInvForeignKey(identity, "library_identity");

    public SLibrary(String variable) {
        super(SLibrary.class, forVariable(variable), "null", "library_");
        addMetadata();
    }

    public SLibrary(String variable, String schema, String table) {
        super(SLibrary.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SLibrary(Path<? extends SLibrary> path) {
        super(path.getType(), path.getMetadata(), "null", "library_");
        addMetadata();
    }

    public SLibrary(PathMetadata<?> metadata) {
        super(SLibrary.class, metadata, "null", "library_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(identity, ColumnMetadata.named("identity").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

