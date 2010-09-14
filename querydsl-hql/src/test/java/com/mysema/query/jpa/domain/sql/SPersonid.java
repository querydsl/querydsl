package com.mysema.query.jpa.domain.sql;

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
 * SPersonid is a Querydsl query type for SPersonid
 */
@Table("PERSONID")
public class SPersonid extends RelationalPathBase<SPersonid> implements RelationalPath<SPersonid> {

    private static final long serialVersionUID = 1500692345;

    public static final SPersonid personid = new SPersonid("PERSONID");

    public final StringPath country = createString("COUNTRY");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Integer> medicarenumber = createNumber("MEDICARENUMBER", Integer.class);

    public final PrimaryKey<SPersonid> sql100819184437170 = createPrimaryKey(id);

    public final ForeignKey<SPerson> _fk8e48877578234709 = new ForeignKey<SPerson>(this, id, "PID_ID");

    public SPersonid(String variable) {
        super(SPersonid.class, forVariable(variable));
    }

    public SPersonid(BeanPath<? extends SPersonid> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SPersonid(PathMetadata<?> metadata) {
        super(SPersonid.class, metadata);
    }

}

