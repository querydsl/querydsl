package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import java.util.*;


/**
 * SBookversion is a Querydsl query type for SBookversion
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SBookversion extends com.mysema.query.sql.RelationalPathBase<SBookversion> {

    private static final long serialVersionUID = -1056272322;

    public static final SBookversion bookversion = new SBookversion("BOOKVERSION_");

    public final NumberPath<Long> bookidIdentity = createNumber("BOOKID_IDENTITY", Long.class);

    public final StringPath description = createString("DESCRIPTION");

    public final NumberPath<Long> libraryIdentity = createNumber("LIBRARY_IDENTITY", Long.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SBookversion> sql120219232320350 = createPrimaryKey(bookidIdentity, libraryIdentity);

    public final com.mysema.query.sql.ForeignKey<SBookBookmarks> _fk94026827e33d3be4 = createInvForeignKey(Arrays.asList(bookidIdentity, bookidIdentity), Arrays.asList("BOOKVERSION_BOOKID_IDENTITY", "BOOKVERSION_BOOKID_IDENTITY"));

    public SBookversion(String variable) {
        super(SBookversion.class, forVariable(variable), "APP", "BOOKVERSION_");
    }

    public SBookversion(Path<? extends SBookversion> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "BOOKVERSION_");
    }

    public SBookversion(PathMetadata<?> metadata) {
        super(SBookversion.class, metadata, "APP", "BOOKVERSION_");
    }

}

