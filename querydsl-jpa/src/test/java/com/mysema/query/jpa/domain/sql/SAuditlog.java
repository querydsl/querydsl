package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SAuditlog is a Querydsl query type for SAuditlog
 */
@Table("AUDITLOG")
public class SAuditlog extends RelationalPathBase<SAuditlog> {

    private static final long serialVersionUID = 2033602002;

    public static final SAuditlog auditlog = new SAuditlog("AUDITLOG");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final NumberPath<Long> itemId = createNumber("ITEM_ID", Long.class);

    public final PrimaryKey<SAuditlog> sql100819184430350 = createPrimaryKey(id);

    public final ForeignKey<SItem> fk3e07a1891bee4d44 = new ForeignKey<SItem>(this, itemId, "ID");

    public SAuditlog(String variable) {
        super(SAuditlog.class, forVariable(variable));
    }

    public SAuditlog(BeanPath<? extends SAuditlog> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SAuditlog(PathMetadata<?> metadata) {
        super(SAuditlog.class, metadata);
    }
    
}

