package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SBook is a Querydsl query type for SBook
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SBook extends com.mysema.query.sql.RelationalPathBase<SBook> {

    private static final long serialVersionUID = -1389563046;

    public static final SBook book = new SBook("book_");

    public final NumberPath<Long> authorId = createNumber("AUTHOR_ID", Long.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath title = createString("TITLE");

    public final com.mysema.query.sql.PrimaryKey<SBook> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SAuthor> book_AUTHORIDFK = createForeignKey(authorId, "ID");

    public SBook(String variable) {
        super(SBook.class, forVariable(variable), "null", "book_");
    }

    public SBook(Path<? extends SBook> path) {
        super(path.getType(), path.getMetadata(), "null", "book_");
    }

    public SBook(PathMetadata<?> metadata) {
        super(SBook.class, metadata, "null", "book_");
    }

}

