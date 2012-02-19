package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SNamed is a Querydsl query type for SNamed
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SNamed extends com.mysema.query.sql.RelationalPathBase<SNamed> {

    private static final long serialVersionUID = -116118296;

    public static final SNamed named = new SNamed("NAMED_");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SNamed> sql120219232326590 = createPrimaryKey(id);

    public SNamed(String variable) {
        super(SNamed.class, forVariable(variable), "APP", "NAMED_");
    }

    public SNamed(Path<? extends SNamed> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "NAMED_");
    }

    public SNamed(PathMetadata<?> metadata) {
        super(SNamed.class, metadata, "APP", "NAMED_");
    }

}

