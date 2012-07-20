package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SAuthor is a Querydsl query type for SAuthor
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SAuthor extends com.mysema.query.sql.RelationalPathBase<SAuthor> {

    private static final long serialVersionUID = 341803452;

    public static final SAuthor author = new SAuthor("author_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SAuthor> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SBook> _book_AUTHORIDFK = createInvForeignKey(id, "AUTHOR_ID");

    public SAuthor(String variable) {
        super(SAuthor.class, forVariable(variable), "null", "author_");
    }

    public SAuthor(Path<? extends SAuthor> path) {
        super(path.getType(), path.getMetadata(), "null", "author_");
    }

    public SAuthor(PathMetadata<?> metadata) {
        super(SAuthor.class, metadata, "null", "author_");
    }

}

