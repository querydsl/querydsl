package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SNamed is a Querydsl query type for SNamed
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SNamed extends com.mysema.query.sql.RelationalPathBase<SNamed> {

    private static final long serialVersionUID = -116118296;

    public static final SNamed named = new SNamed("named_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SNamed> primary = createPrimaryKey(id);

    public SNamed(String variable) {
        super(SNamed.class, forVariable(variable), "null", "named_");
    }

    public SNamed(Path<? extends SNamed> path) {
        super(path.getType(), path.getMetadata(), "null", "named_");
    }

    public SNamed(PathMetadata<?> metadata) {
        super(SNamed.class, metadata, "null", "named_");
    }

}

