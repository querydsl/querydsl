package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SAuthor is a Querydsl querydsl type for SAuthor
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SAuthor extends com.querydsl.sql.RelationalPathBase<SAuthor> {

    private static final long serialVersionUID = 2005972515;

    public static final SAuthor author_ = new SAuthor("author_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<SAuthor> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SBook> _fk599229686eaf51c = createInvForeignKey(id, "AUTHOR_ID");

    public SAuthor(String variable) {
        super(SAuthor.class, forVariable(variable), "", "author_");
        addMetadata();
    }

    public SAuthor(String variable, String schema, String table) {
        super(SAuthor.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SAuthor(Path<? extends SAuthor> path) {
        super(path.getType(), path.getMetadata(), "", "author_");
        addMetadata();
    }

    public SAuthor(PathMetadata<?> metadata) {
        super(SAuthor.class, metadata, "", "author_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
    }

}

