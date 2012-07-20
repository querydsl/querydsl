package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import java.util.*;


/**
 * SBookversion is a Querydsl query type for SBookversion
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SBookversion extends com.mysema.query.sql.RelationalPathBase<SBookversion> {

    private static final long serialVersionUID = -1056272322;

    public static final SBookversion bookversion = new SBookversion("bookversion_");

    public final NumberPath<Long> bookidIdentity = createNumber("BOOKID_IDENTITY", Long.class);

    public final StringPath description = createString("DESCRIPTION");

    public final NumberPath<Long> libraryIdentity = createNumber("LIBRARY_IDENTITY", Long.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SBookversion> primary = createPrimaryKey(bookidIdentity, libraryIdentity);

    public final com.mysema.query.sql.ForeignKey<SLibrary> bookversion_LIBRARYIDENTITYFK = createForeignKey(libraryIdentity, "IDENTITY");

    public final com.mysema.query.sql.ForeignKey<SBookid> bookversion_BOOKIDIDENTITYFK = createForeignKey(bookidIdentity, "IDENTITY");

    public final com.mysema.query.sql.ForeignKey<SBookBookmarks> _fk94026827e33d3be4 = createInvForeignKey(Arrays.asList(bookidIdentity, bookidIdentity), Arrays.asList("BookVersion_bookID_identity", "BookVersion_bookID_identity"));

    public final com.mysema.query.sql.ForeignKey<SBookBookmarks> _bookBookmarksBOOKIDIDENTITYFK = createInvForeignKey(Arrays.asList(bookidIdentity, bookidIdentity), Arrays.asList("BOOKID_IDENTITY", "BOOKID_IDENTITY"));

    public SBookversion(String variable) {
        super(SBookversion.class, forVariable(variable), "null", "bookversion_");
    }

    public SBookversion(Path<? extends SBookversion> path) {
        super(path.getType(), path.getMetadata(), "null", "bookversion_");
    }

    public SBookversion(PathMetadata<?> metadata) {
        super(SBookversion.class, metadata, "null", "bookversion_");
    }

}

