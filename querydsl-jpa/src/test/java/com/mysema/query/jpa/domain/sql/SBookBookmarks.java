package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import java.util.*;


/**
 * SBookBookmarks is a Querydsl query type for SBookBookmarks
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SBookBookmarks extends com.mysema.query.sql.RelationalPathBase<SBookBookmarks> {

    private static final long serialVersionUID = -312126525;

    public static final SBookBookmarks bookBookmarks = new SBookBookmarks("BOOK_BOOKMARKS");

    public final NumberPath<Integer> bookmarksOrder = createNumber("BOOKMARKS_ORDER", Integer.class);

    public final NumberPath<Long> bookversionBookidIdentity = createNumber("BOOKVERSION_BOOKID_IDENTITY", Long.class);

    public final NumberPath<Long> bookversionLibraryIdentity = createNumber("BOOKVERSION_LIBRARY_IDENTITY", Long.class);

    public final StringPath comment = createString("COMMENT");

    public final NumberPath<Long> page = createNumber("PAGE", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SBookBookmarks> sql120219232320100 = createPrimaryKey(bookmarksOrder, bookversionBookidIdentity, bookversionLibraryIdentity);

    public final com.mysema.query.sql.ForeignKey<SBookversion> fk94026827e33d3be4 = createForeignKey(Arrays.asList(bookversionBookidIdentity, bookversionBookidIdentity), Arrays.asList("BOOKID_IDENTITY", "BOOKID_IDENTITY"));

    public SBookBookmarks(String variable) {
        super(SBookBookmarks.class, forVariable(variable), "APP", "BOOK_BOOKMARKS");
    }

    public SBookBookmarks(Path<? extends SBookBookmarks> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "BOOK_BOOKMARKS");
    }

    public SBookBookmarks(PathMetadata<?> metadata) {
        super(SBookBookmarks.class, metadata, "APP", "BOOK_BOOKMARKS");
    }

}

