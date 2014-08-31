package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SBook is a Querydsl query type for SBook
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SBook extends com.mysema.query.sql.RelationalPathBase<SBook> {

    private static final long serialVersionUID = -126781371;

    public static final SBook book_ = new SBook("book_");

    public final NumberPath<Long> authorId = createNumber("authorId", Long.class);

    public final StringPath dtype = createString("dtype");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public final com.mysema.query.sql.PrimaryKey<SBook> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SAuthor> fk599229686eaf51c = createForeignKey(authorId, "id");

    public SBook(String variable) {
        super(SBook.class, forVariable(variable), "", "book_");
        addMetadata();
    }

    public SBook(String variable, String schema, String table) {
        super(SBook.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SBook(Path<? extends SBook> path) {
        super(path.getType(), path.getMetadata(), "", "book_");
        addMetadata();
    }

    public SBook(PathMetadata<?> metadata) {
        super(SBook.class, metadata, "", "book_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(authorId, ColumnMetadata.named("AUTHOR_ID").withIndex(4).ofType(-5).withSize(19));
        addMetadata(dtype, ColumnMetadata.named("DTYPE").withIndex(1).ofType(12).withSize(31).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(title, ColumnMetadata.named("title").withIndex(3).ofType(12).withSize(255));
    }

}

