package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.DatePath;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SBar is a Querydsl querydsl type for SBar
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SBar extends com.querydsl.sql.RelationalPathBase<SBar> {

    private static final long serialVersionUID = -1389576419;

    public static final SBar bar_ = new SBar("bar_");

    public final DatePath<java.sql.Date> date = createDate("date", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.querydsl.sql.PrimaryKey<SBar> primary = createPrimaryKey(id);

    public SBar(String variable) {
        super(SBar.class, forVariable(variable), "", "bar_");
        addMetadata();
    }

    public SBar(String variable, String schema, String table) {
        super(SBar.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SBar(Path<? extends SBar> path) {
        super(path.getType(), path.getMetadata(), "", "bar_");
        addMetadata();
    }

    public SBar(PathMetadata<?> metadata) {
        super(SBar.class, metadata, "", "bar_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(date, ColumnMetadata.named("date").withIndex(2).ofType(91).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
    }

}

