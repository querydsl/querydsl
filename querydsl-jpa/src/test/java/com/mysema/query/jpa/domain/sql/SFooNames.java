package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SFooNames is a Querydsl query type for SFooNames
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SFooNames extends com.mysema.query.sql.RelationalPathBase<SFooNames> {

    private static final long serialVersionUID = 982089235;

    public static final SFooNames fooNames = new SFooNames("foo_names");

    public final NumberPath<Integer> fooId = createNumber("foo_id", Integer.class);

    public final StringPath names = createString("NAMES");

    public final com.mysema.query.sql.ForeignKey<SFoo> fooNamesFooIdFK = createForeignKey(fooId, "ID");

    public SFooNames(String variable) {
        super(SFooNames.class, forVariable(variable), "null", "foo_names");
    }

    public SFooNames(Path<? extends SFooNames> path) {
        super(path.getType(), path.getMetadata(), "null", "foo_names");
    }

    public SFooNames(PathMetadata<?> metadata) {
        super(SFooNames.class, metadata, "null", "foo_names");
    }

}

