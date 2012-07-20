package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SBookid is a Querydsl query type for SBookid
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SBookid extends com.mysema.query.sql.RelationalPathBase<SBookid> {

    private static final long serialVersionUID = 364745205;

    public static final SBookid bookid = new SBookid("bookid_");

    public final NumberPath<Long> identity = createNumber("IDENTITY", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SBookid> primary = createPrimaryKey(identity);

    public final com.mysema.query.sql.ForeignKey<SBookversion> _bookversion_BOOKIDIDENTITYFK = createInvForeignKey(identity, "BOOKID_IDENTITY");

    public SBookid(String variable) {
        super(SBookid.class, forVariable(variable), "null", "bookid_");
    }

    public SBookid(Path<? extends SBookid> path) {
        super(path.getType(), path.getMetadata(), "null", "bookid_");
    }

    public SBookid(PathMetadata<?> metadata) {
        super(SBookid.class, metadata, "null", "bookid_");
    }

}

