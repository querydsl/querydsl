package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SNamed is a Querydsl querydsl type for SNamed
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SNamed extends com.querydsl.sql.RelationalPathBase<SNamed> {

    private static final long serialVersionUID = 695300215;

    public static final SNamed named_ = new SNamed("named_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<SNamed> primary = createPrimaryKey(id);

    public SNamed(String variable) {
        super(SNamed.class, forVariable(variable), "", "named_");
        addMetadata();
    }

    public SNamed(String variable, String schema, String table) {
        super(SNamed.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SNamed(Path<? extends SNamed> path) {
        super(path.getType(), path.getMetadata(), "", "named_");
        addMetadata();
    }

    public SNamed(PathMetadata<?> metadata) {
        super(SNamed.class, metadata, "", "named_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
    }

}

