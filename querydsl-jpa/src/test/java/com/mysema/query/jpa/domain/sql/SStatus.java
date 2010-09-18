package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

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
 * SStatus is a Querydsl query type for SStatus
 */
@Table("STATUS")
public class SStatus extends RelationalPathBase<SStatus> implements RelationalPath<SStatus> {

    private static final long serialVersionUID = 646047355;

    public static final SStatus status = new SStatus("STATUS");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final PrimaryKey<SStatus> sql100819184438880 = createPrimaryKey(id);

    public final ForeignKey<SItem> _fk22ef33eedeba64 = new ForeignKey<SItem>(this, id, "STATUS_ID");

    public final ForeignKey<SItem> _fk22ef33bb4e150b = new ForeignKey<SItem>(this, id, "CURRENTSTATUS_ID");

    public SStatus(String variable) {
        super(SStatus.class, forVariable(variable));
    }

    public SStatus(BeanPath<? extends SStatus> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SStatus(PathMetadata<?> metadata) {
        super(SStatus.class, metadata);
    }

}

