package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SFoo is a Querydsl query type for SFoo
 */
@Table("FOO")
public class SFoo extends RelationalPathBase<SFoo> {

    private static final long serialVersionUID = 1401629405;

    public static final SFoo foo = new SFoo("FOO");

    public final StringPath bar = createString("BAR");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final DatePath<java.sql.Date> startdate = createDate("STARTDATE", java.sql.Date.class);

    public final PrimaryKey<SFoo> sql100819184433460 = createPrimaryKey(id);

    public SFoo(String variable) {
        super(SFoo.class, forVariable(variable));
    }

    public SFoo(BeanPath<? extends SFoo> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SFoo(PathMetadata<?> metadata) {
        super(SFoo.class, metadata);
    }

}

