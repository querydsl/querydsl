package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SBookid is a Querydsl query type for SBookid
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SBookid extends com.mysema.query.sql.RelationalPathBase<SBookid> {

    private static final long serialVersionUID = -1577800438;

    public static final SBookid bookid_ = new SBookid("bookid_");

    public final NumberPath<Long> identity = createNumber("identity", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SBookid> primary = createPrimaryKey(identity);

    public final com.mysema.query.sql.ForeignKey<SBookversion> _fkef4bc0704dd5d6c3 = createInvForeignKey(identity, "bookID_identity");

    public SBookid(String variable) {
        super(SBookid.class, forVariable(variable), "", "bookid_");
        addMetadata();
    }

    public SBookid(String variable, String schema, String table) {
        super(SBookid.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SBookid(Path<? extends SBookid> path) {
        super(path.getType(), path.getMetadata(), "", "bookid_");
        addMetadata();
    }

    public SBookid(PathMetadata<?> metadata) {
        super(SBookid.class, metadata, "", "bookid_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(identity, ColumnMetadata.named("identity").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

