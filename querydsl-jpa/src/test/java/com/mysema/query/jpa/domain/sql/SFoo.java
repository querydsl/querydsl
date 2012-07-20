package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SFoo is a Querydsl query type for SFoo
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SFoo extends com.mysema.query.sql.RelationalPathBase<SFoo> {

    private static final long serialVersionUID = -1291746763;

    public static final SFoo foo = new SFoo("foo_");

    public final StringPath bar = createString("BAR");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final DatePath<java.sql.Date> startdate = createDate("STARTDATE", java.sql.Date.class);

    public final com.mysema.query.sql.PrimaryKey<SFoo> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SFooNames> _fooNamesFooIdFK = createInvForeignKey(id, "foo_id");

    public SFoo(String variable) {
        super(SFoo.class, forVariable(variable), "null", "foo_");
    }

    public SFoo(Path<? extends SFoo> path) {
        super(path.getType(), path.getMetadata(), "null", "foo_");
    }

    public SFoo(PathMetadata<?> metadata) {
        super(SFoo.class, metadata, "null", "foo_");
    }

}

