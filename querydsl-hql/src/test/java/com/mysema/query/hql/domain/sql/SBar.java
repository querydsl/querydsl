package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;


/**
 * SBar is a Querydsl query type for SBar
 */
@Table("BAR")
public class SBar extends RelationalPathBase<SBar> {

    private static final long serialVersionUID = 1401625130;

    public static final SBar bar = new SBar("BAR");

    public final DatePath<java.sql.Date> date = createDate("DATE", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final PrimaryKey<SBar> sql100819184430740 = createPrimaryKey(id);

    public SBar(String variable) {
        super(SBar.class, forVariable(variable));
    }

    public SBar(BeanPath<? extends SBar> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SBar(PathMetadata<?> metadata) {
        super(SBar.class, metadata);
    }

}

