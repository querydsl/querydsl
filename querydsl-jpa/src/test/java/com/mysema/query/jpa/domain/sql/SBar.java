package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SBar is a Querydsl query type for SBar
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SBar extends com.mysema.query.sql.RelationalPathBase<SBar> {

    private static final long serialVersionUID = -1291751038;

    public static final SBar bar = new SBar("bar_");

    public final DatePath<java.sql.Date> date = createDate("DATE", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SBar> primary = createPrimaryKey(id);

    public SBar(String variable) {
        super(SBar.class, forVariable(variable), "null", "bar_");
    }

    public SBar(Path<? extends SBar> path) {
        super(path.getType(), path.getMetadata(), "null", "bar_");
    }

    public SBar(PathMetadata<?> metadata) {
        super(SBar.class, metadata, "null", "bar_");
    }

}

