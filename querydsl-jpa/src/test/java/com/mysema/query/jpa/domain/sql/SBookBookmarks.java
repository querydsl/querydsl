package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import java.util.*;


/**
 * SBookBookmarks is a Querydsl query type for SBookBookmarks
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SBookBookmarks extends com.mysema.query.sql.RelationalPathBase<SBookBookmarks> {

    private static final long serialVersionUID = -312126525;

    public static final SBookBookmarks bookBookmarks = new SBookBookmarks("book_bookmarks");

    public final NumberPath<Long> bookidIdentity = createNumber("BOOKID_IDENTITY", Long.class);

    public final NumberPath<Integer> bookMarksORDER = createNumber("bookMarks_ORDER", Integer.class);

    public final NumberPath<Long> bookVersionBookIDIdentity = createNumber("BookVersion_bookID_identity", Long.class);

    public final NumberPath<Long> bookVersionLibraryIdentity = createNumber("BookVersion_library_identity", Long.class);

    public final StringPath comment = createString("COMMENT");

    public final NumberPath<Long> libraryIdentity = createNumber("LIBRARY_IDENTITY", Long.class);

    public final NumberPath<Long> page = createNumber("PAGE", Long.class);

    public final com.mysema.query.sql.ForeignKey<SBookversion> fk94026827e33d3be4 = createForeignKey(Arrays.asList(bookVersionBookIDIdentity, bookVersionBookIDIdentity), Arrays.asList("BOOKID_IDENTITY", "BOOKID_IDENTITY"));

    public final com.mysema.query.sql.ForeignKey<SBookversion> bookBookmarksBOOKIDIDENTITYFK = createForeignKey(Arrays.asList(bookidIdentity, bookidIdentity), Arrays.asList("BOOKID_IDENTITY", "BOOKID_IDENTITY"));

    public SBookBookmarks(String variable) {
        super(SBookBookmarks.class, forVariable(variable), "null", "book_bookmarks");
    }

    public SBookBookmarks(Path<? extends SBookBookmarks> path) {
        super(path.getType(), path.getMetadata(), "null", "book_bookmarks");
    }

    public SBookBookmarks(PathMetadata<?> metadata) {
        super(SBookBookmarks.class, metadata, "null", "book_bookmarks");
    }

}

