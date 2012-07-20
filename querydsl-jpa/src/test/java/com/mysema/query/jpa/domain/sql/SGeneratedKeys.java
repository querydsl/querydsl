package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SGeneratedKeys is a Querydsl query type for SGeneratedKeys
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SGeneratedKeys extends com.mysema.query.sql.RelationalPathBase<SGeneratedKeys> {

    private static final long serialVersionUID = 379851474;

    public static final SGeneratedKeys generatedKeys = new SGeneratedKeys("GENERATED_KEYS");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SGeneratedKeys> primary = createPrimaryKey(id);

    public SGeneratedKeys(String variable) {
        super(SGeneratedKeys.class, forVariable(variable), "null", "GENERATED_KEYS");
    }

    public SGeneratedKeys(Path<? extends SGeneratedKeys> path) {
        super(path.getType(), path.getMetadata(), "null", "GENERATED_KEYS");
    }

    public SGeneratedKeys(PathMetadata<?> metadata) {
        super(SGeneratedKeys.class, metadata, "null", "GENERATED_KEYS");
    }

}

