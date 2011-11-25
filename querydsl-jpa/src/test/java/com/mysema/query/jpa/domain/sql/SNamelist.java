package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SNamelist is a Querydsl query type for SNamelist
 */
public class SNamelist extends RelationalPathBase<SNamelist> implements RelationalPath<SNamelist> {

    private static final long serialVersionUID = -1396144654;

    public static final SNamelist namelist = new SNamelist("NAMELIST");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final PrimaryKey<SNamelist> sql100819184435410 = createPrimaryKey(id);

    public final ForeignKey<SNamelistNames> _fkd6c82d72b8406ca4 = new ForeignKey<SNamelistNames>(this, id, "NAMELIST_ID");

    public SNamelist(String variable) {
        super(SNamelist.class, forVariable(variable), null, "NAMELIST");
    }

    public SNamelist(BeanPath<? extends SNamelist> entity) {
        super(entity.getType(), entity.getMetadata(), null, "NAMELIST");
    }

    public SNamelist(PathMetadata<?> metadata) {
        super(SNamelist.class, metadata, null, "NAMELIST");
    }

}

