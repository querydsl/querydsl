package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SFooNames is a Querydsl query type for SFooNames
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SFooNames extends com.mysema.query.sql.RelationalPathBase<SFooNames> {

    private static final long serialVersionUID = 982089235;

    public static final SFooNames fooNames = new SFooNames("FOO_NAMES");

    public final NumberPath<Integer> fooId = createNumber("FOO_ID", Integer.class);

    public final StringPath names = createString("NAMES");

    public final com.mysema.query.sql.ForeignKey<SFoo> fkb6129a8f94e297f8 = createForeignKey(fooId, "ID");

    public SFooNames(String variable) {
        super(SFooNames.class, forVariable(variable), "APP", "FOO_NAMES");
    }

    public SFooNames(Path<? extends SFooNames> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "FOO_NAMES");
    }

    public SFooNames(PathMetadata<?> metadata) {
        super(SFooNames.class, metadata, "APP", "FOO_NAMES");
    }

}

