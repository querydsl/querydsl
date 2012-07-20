package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SParent is a Querydsl query type for SParent
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SParent extends com.mysema.query.sql.RelationalPathBase<SParent> {

    private static final long serialVersionUID = 752707803;

    public static final SParent parent = new SParent("parent_");

    public final StringPath childname = createString("CHILDNAME");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SParent> primary = createPrimaryKey(id);

    public SParent(String variable) {
        super(SParent.class, forVariable(variable), "null", "parent_");
    }

    public SParent(Path<? extends SParent> path) {
        super(path.getType(), path.getMetadata(), "null", "parent_");
    }

    public SParent(PathMetadata<?> metadata) {
        super(SParent.class, metadata, "null", "parent_");
    }

}

