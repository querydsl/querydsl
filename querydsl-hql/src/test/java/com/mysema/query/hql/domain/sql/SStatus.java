package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SStatus is a Querydsl query type for SStatus
 */
@Table("STATUS")
public class SStatus extends BeanPath<SStatus> implements RelationalPath<SStatus> {

    private static final long serialVersionUID = 646047355;

    public static final SStatus status = new SStatus("STATUS");

    public final PNumber<Long> id = createNumber("ID", Long.class);

    public final PString name = createString("NAME");

    private Expr[] _all;

    public final PrimaryKey<SStatus> sql100819184438880 = new PrimaryKey<SStatus>(this, id);

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

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{id, name};
        }
        return _all;
    }

    public PrimaryKey<SStatus> getPrimaryKey() {
        return sql100819184438880;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk22ef33eedeba64, _fk22ef33bb4e150b);
    }

}

