package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import java.util.*;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SBookversion is a Querydsl query type for SBookversion
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SBookversion extends com.mysema.query.sql.RelationalPathBase<SBookversion> {

    private static final long serialVersionUID = 1615296481;

    public static final SBookversion bookversion_ = new SBookversion("bookversion_");

    public final NumberPath<Long> bookIDIdentity = createNumber("bookIDIdentity", Long.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> libraryIdentity = createNumber("libraryIdentity", Long.class);

    public final StringPath name = createString("name");

    public final com.mysema.query.sql.PrimaryKey<SBookversion> primary = createPrimaryKey(bookIDIdentity, libraryIdentity);

    public final com.mysema.query.sql.ForeignKey<SBookid> fkef4bc0704dd5d6c3 = createForeignKey(bookIDIdentity, "identity");

    public final com.mysema.query.sql.ForeignKey<SLibrary> fkef4bc070e364cd17 = createForeignKey(libraryIdentity, "identity");

    public final com.mysema.query.sql.ForeignKey<SBookBookmarks> _fk94026827e33d3be4 = createInvForeignKey(Arrays.asList(bookIDIdentity, libraryIdentity), Arrays.asList("BookVersion_bookID_identity", "BookVersion_library_identity"));

    public SBookversion(String variable) {
        super(SBookversion.class, forVariable(variable), "null", "bookversion_");
        addMetadata();
    }

    public SBookversion(String variable, String schema, String table) {
        super(SBookversion.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SBookversion(Path<? extends SBookversion> path) {
        super(path.getType(), path.getMetadata(), "null", "bookversion_");
        addMetadata();
    }

    public SBookversion(PathMetadata<?> metadata) {
        super(SBookversion.class, metadata, "null", "bookversion_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bookIDIdentity, ColumnMetadata.named("bookID_identity").withIndex(3).ofType(-5).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(1).ofType(12).withSize(255));
        addMetadata(libraryIdentity, ColumnMetadata.named("library_identity").withIndex(4).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
    }

}

