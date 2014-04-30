package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import java.util.*;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SBookBookmarks is a Querydsl query type for SBookBookmarks
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SBookBookmarks extends com.mysema.query.sql.RelationalPathBase<SBookBookmarks> {

    private static final long serialVersionUID = -312126525;

    public static final SBookBookmarks bookBookmarks = new SBookBookmarks("book_bookmarks");

    public final NumberPath<Integer> bookMarksORDER = createNumber("bookMarksORDER", Integer.class);

    public final NumberPath<Long> bookVersionBookIDIdentity = createNumber("bookVersionBookIDIdentity", Long.class);

    public final NumberPath<Long> bookVersionLibraryIdentity = createNumber("bookVersionLibraryIdentity", Long.class);

    public final StringPath comment = createString("comment");

    public final NumberPath<Long> page = createNumber("page", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SBookBookmarks> primary = createPrimaryKey(bookVersionBookIDIdentity, bookVersionLibraryIdentity, bookMarksORDER);

    public final com.mysema.query.sql.ForeignKey<SBookversion> fk94026827e33d3be4 = createForeignKey(Arrays.asList(bookVersionBookIDIdentity, bookVersionLibraryIdentity), Arrays.asList("bookID_identity", "library_identity"));

    public SBookBookmarks(String variable) {
        super(SBookBookmarks.class, forVariable(variable), "null", "book_bookmarks");
        addMetadata();
    }

    public SBookBookmarks(String variable, String schema, String table) {
        super(SBookBookmarks.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SBookBookmarks(Path<? extends SBookBookmarks> path) {
        super(path.getType(), path.getMetadata(), "null", "book_bookmarks");
        addMetadata();
    }

    public SBookBookmarks(PathMetadata<?> metadata) {
        super(SBookBookmarks.class, metadata, "null", "book_bookmarks");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bookMarksORDER, ColumnMetadata.named("bookMarks_ORDER").withIndex(5).ofType(4).withSize(10).notNull());
        addMetadata(bookVersionBookIDIdentity, ColumnMetadata.named("BookVersion_bookID_identity").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(bookVersionLibraryIdentity, ColumnMetadata.named("BookVersion_library_identity").withIndex(2).ofType(-5).withSize(19).notNull());
        addMetadata(comment, ColumnMetadata.named("comment").withIndex(3).ofType(12).withSize(255));
        addMetadata(page, ColumnMetadata.named("page").withIndex(4).ofType(-5).withSize(19));
    }

}

