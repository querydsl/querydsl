package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PathMetadata;

/**
 * SAuditlog is a Querydsl query type for SAuditlog
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="AUDITLOG")
public class SAuditlog extends PEntity<SAuditlog> {

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PNumber<Long> itemId = createNumber("ITEM_ID", Long.class);

    public SAuditlog(String variable) {
        super(SAuditlog.class, forVariable(variable));
    }

    public SAuditlog(PEntity<? extends SAuditlog> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SAuditlog(PathMetadata<?> metadata) {
        super(SAuditlog.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

