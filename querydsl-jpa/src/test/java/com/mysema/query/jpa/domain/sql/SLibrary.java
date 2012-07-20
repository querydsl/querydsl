package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SLibrary is a Querydsl query type for SLibrary
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SLibrary extends com.mysema.query.sql.RelationalPathBase<SLibrary> {

    private static final long serialVersionUID = -1476277590;

    public static final SLibrary library = new SLibrary("library_");

    public final NumberPath<Long> identity = createNumber("IDENTITY", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SLibrary> primary = createPrimaryKey(identity);

    public final com.mysema.query.sql.ForeignKey<SBookversion> _bookversion_LIBRARYIDENTITYFK = createInvForeignKey(identity, "LIBRARY_IDENTITY");

    public SLibrary(String variable) {
        super(SLibrary.class, forVariable(variable), "null", "library_");
    }

    public SLibrary(Path<? extends SLibrary> path) {
        super(path.getType(), path.getMetadata(), "null", "library_");
    }

    public SLibrary(PathMetadata<?> metadata) {
        super(SLibrary.class, metadata, "null", "library_");
    }

}

