package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;
import java.util.Arrays;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SBookversion is a Querydsl querydsl type for SBookversion
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SBookversion extends com.querydsl.sql.RelationalPathBase<SBookversion> {

    private static final long serialVersionUID = 1615296481;

    public static final SBookversion bookversion_ = new SBookversion("bookversion_");

    public final NumberPath<Long> bookIDIdentity = createNumber("bookIDIdentity", Long.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> libraryIdentity = createNumber("libraryIdentity", Long.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<SBookversion> primary = createPrimaryKey(bookIDIdentity, libraryIdentity);

    public final com.querydsl.sql.ForeignKey<SBookid> fkef4bc0704dd5d6c3 = createForeignKey(bookIDIdentity, "identity");

    public final com.querydsl.sql.ForeignKey<SLibrary> fkef4bc070e364cd17 = createForeignKey(libraryIdentity, "identity");

    public final com.querydsl.sql.ForeignKey<SBookBookmarks> _fk94026827e33d3be4 = createInvForeignKey(Arrays.asList(bookIDIdentity, libraryIdentity), Arrays.asList("BookVersion_bookID_identity", "BookVersion_library_identity"));

    public SBookversion(String variable) {
        super(SBookversion.class, forVariable(variable), "", "bookversion_");
        addMetadata();
    }

    public SBookversion(String variable, String schema, String table) {
        super(SBookversion.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SBookversion(Path<? extends SBookversion> path) {
        super(path.getType(), path.getMetadata(), "", "bookversion_");
        addMetadata();
    }

    public SBookversion(PathMetadata<?> metadata) {
        super(SBookversion.class, metadata, "", "bookversion_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bookIDIdentity, ColumnMetadata.named("bookID_identity").withIndex(3).ofType(-5).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(1).ofType(12).withSize(255));
        addMetadata(libraryIdentity, ColumnMetadata.named("library_identity").withIndex(4).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
    }

}

