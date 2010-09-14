package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SShowActs is a Querydsl query type for SShowActs
 */
@Table("SHOW_ACTS")
public class SShowActs extends RelationalPathBase<SShowActs> implements RelationalPath<SShowActs> {

    private static final long serialVersionUID = 718125831;

    public static final SShowActs showActs = new SShowActs("SHOW_ACTS");

    public final StringPath actsKey = createString("ACTS_KEY");

    public final StringPath element = createString("ELEMENT");

    public final NumberPath<Integer> showId = createNumber("SHOW_ID", Integer.class);

    public final PrimaryKey<SShowActs> sql100819184438340 = createPrimaryKey(actsKey, showId);

    public final ForeignKey<SShow> fk5f6ee0319084d04 = new ForeignKey<SShow>(this, showId, "ID");

    public SShowActs(String variable) {
        super(SShowActs.class, forVariable(variable));
    }

    public SShowActs(BeanPath<? extends SShowActs> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SShowActs(PathMetadata<?> metadata) {
        super(SShowActs.class, metadata);
    }

}

