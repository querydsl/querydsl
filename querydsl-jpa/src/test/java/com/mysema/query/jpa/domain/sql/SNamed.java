package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SNamed is a Querydsl query type for SNamed
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SNamed extends com.mysema.query.sql.RelationalPathBase<SNamed> {

    private static final long serialVersionUID = 695300215;

    public static final SNamed named_ = new SNamed("named_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.mysema.query.sql.PrimaryKey<SNamed> primary = createPrimaryKey(id);

    public SNamed(String variable) {
        super(SNamed.class, forVariable(variable), "null", "named_");
        addMetadata();
    }

    public SNamed(String variable, String schema, String table) {
        super(SNamed.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SNamed(Path<? extends SNamed> path) {
        super(path.getType(), path.getMetadata(), "null", "named_");
        addMetadata();
    }

    public SNamed(PathMetadata<?> metadata) {
        super(SNamed.class, metadata, "null", "named_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
    }

}

