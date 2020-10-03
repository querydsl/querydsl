package com.querydsl.jpa.domain.sql;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.ColumnMetadata;


/**
 * SLibrary is a Querydsl query type for SLibrary
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SLibrary extends com.querydsl.sql.RelationalPathBase<SLibrary> {

    private static final long serialVersionUID = 1480035061;

    public static final SLibrary library_ = new SLibrary("library_");

    public final NumberPath<Long> identity = createNumber("identity", Long.class);

    public final com.querydsl.sql.PrimaryKey<SLibrary> primary = createPrimaryKey(identity);

    public final com.querydsl.sql.ForeignKey<SBookversion> _fkef4bc070e364cd17 = createInvForeignKey(identity, "library_identity");

    public SLibrary(String variable) {
        super(SLibrary.class, forVariable(variable), "", "library_");
        addMetadata();
    }

    public SLibrary(String variable, String schema, String table) {
        super(SLibrary.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SLibrary(Path<? extends SLibrary> path) {
        super(path.getType(), path.getMetadata(), "", "library_");
        addMetadata();
    }

    public SLibrary(PathMetadata metadata) {
        super(SLibrary.class, metadata, "", "library_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(identity, ColumnMetadata.named("identity").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

