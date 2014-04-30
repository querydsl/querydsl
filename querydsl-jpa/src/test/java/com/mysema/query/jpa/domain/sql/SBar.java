package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SBar is a Querydsl query type for SBar
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SBar extends com.mysema.query.sql.RelationalPathBase<SBar> {

    private static final long serialVersionUID = -1389576419;

    public static final SBar bar_ = new SBar("bar_");

    public final DatePath<java.sql.Date> date = createDate("date", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SBar> primary = createPrimaryKey(id);

    public SBar(String variable) {
        super(SBar.class, forVariable(variable), "null", "bar_");
        addMetadata();
    }

    public SBar(String variable, String schema, String table) {
        super(SBar.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SBar(Path<? extends SBar> path) {
        super(path.getType(), path.getMetadata(), "null", "bar_");
        addMetadata();
    }

    public SBar(PathMetadata<?> metadata) {
        super(SBar.class, metadata, "null", "bar_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(date, ColumnMetadata.named("date").withIndex(2).ofType(91).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
    }

}

