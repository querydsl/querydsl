package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SLanguage is a Querydsl query type for SLanguage
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SLanguage extends com.mysema.query.sql.RelationalPathBase<SLanguage> {

    private static final long serialVersionUID = -991057079;

    public static final SLanguage language = new SLanguage("language");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath text = createString("text");

    public final com.mysema.query.sql.PrimaryKey<SLanguage> primary = createPrimaryKey(id);

    public SLanguage(String variable) {
        super(SLanguage.class, forVariable(variable), "null", "language");
    }

    public SLanguage(Path<? extends SLanguage> path) {
        super(path.getType(), path.getMetadata(), "null", "language");
    }

    public SLanguage(PathMetadata<?> metadata) {
        super(SLanguage.class, metadata, "null", "language");
    }

}

