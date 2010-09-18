package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SNamelistNames is a Querydsl query type for SNamelistNames
 */
@Table("NAMELIST_NAMES")
public class SNamelistNames extends RelationalPathBase<SNamelistNames> implements RelationalPath<SNamelistNames> {

    private static final long serialVersionUID = -1506785162;

    public static final SNamelistNames namelistNames = new SNamelistNames("NAMELIST_NAMES");

    public final StringPath element = createString("ELEMENT");

    public final NumberPath<Long> namelistId = createNumber("NAMELIST_ID", Long.class);

    public final ForeignKey<SNamelist> fkd6c82d72b8406ca4 = new ForeignKey<SNamelist>(this, namelistId, "ID");

    public SNamelistNames(String variable) {
        super(SNamelistNames.class, forVariable(variable));
    }

    public SNamelistNames(BeanPath<? extends SNamelistNames> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SNamelistNames(PathMetadata<?> metadata) {
        super(SNamelistNames.class, metadata);
    }

}

