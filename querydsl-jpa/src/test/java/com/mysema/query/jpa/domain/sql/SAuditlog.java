package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SAuditlog is a Querydsl query type for SAuditlog
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SAuditlog extends com.mysema.query.sql.RelationalPathBase<SAuditlog> {

    private static final long serialVersionUID = 1598606714;

    public static final SAuditlog auditlog = new SAuditlog("AUDITLOG_");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final NumberPath<Long> itemId = createNumber("ITEM_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SAuditlog> sql120219232319680 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SItem> fkb88fbf6ae26109c = createForeignKey(itemId, "ID");

    public SAuditlog(String variable) {
        super(SAuditlog.class, forVariable(variable), "APP", "AUDITLOG_");
    }

    public SAuditlog(Path<? extends SAuditlog> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "AUDITLOG_");
    }

    public SAuditlog(PathMetadata<?> metadata) {
        super(SAuditlog.class, metadata, "APP", "AUDITLOG_");
    }

}

