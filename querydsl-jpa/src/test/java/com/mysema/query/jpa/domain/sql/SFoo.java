package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SFoo is a Querydsl query type for SFoo
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SFoo extends com.mysema.query.sql.RelationalPathBase<SFoo> {

    private static final long serialVersionUID = -1291746763;

    public static final SFoo foo = new SFoo("FOO_");

    public final StringPath bar = createString("BAR");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final DatePath<java.sql.Date> startdate = createDate("STARTDATE", java.sql.Date.class);

    public final com.mysema.query.sql.PrimaryKey<SFoo> sql120219232324250 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SFooNames> _fkb6129a8f94e297f8 = createInvForeignKey(id, "FOO_ID");

    public SFoo(String variable) {
        super(SFoo.class, forVariable(variable), "APP", "FOO_");
    }

    public SFoo(Path<? extends SFoo> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "FOO_");
    }

    public SFoo(PathMetadata<?> metadata) {
        super(SFoo.class, metadata, "APP", "FOO_");
    }

}

