package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SAuthor is a Querydsl query type for SAuthor
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SAuthor extends com.mysema.query.sql.RelationalPathBase<SAuthor> {

    private static final long serialVersionUID = 2005972515;

    public static final SAuthor author_ = new SAuthor("author_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.mysema.query.sql.PrimaryKey<SAuthor> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SBook> _fk599229686eaf51c = createInvForeignKey(id, "AUTHOR_ID");

    public SAuthor(String variable) {
        super(SAuthor.class, forVariable(variable), "null", "author_");
        addMetadata();
    }

    public SAuthor(String variable, String schema, String table) {
        super(SAuthor.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SAuthor(Path<? extends SAuthor> path) {
        super(path.getType(), path.getMetadata(), "null", "author_");
        addMetadata();
    }

    public SAuthor(PathMetadata<?> metadata) {
        super(SAuthor.class, metadata, "null", "author_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
    }

}

