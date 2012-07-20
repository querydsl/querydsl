package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SAuditlog is a Querydsl query type for SAuditlog
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SAuditlog extends com.mysema.query.sql.RelationalPathBase<SAuditlog> {

    private static final long serialVersionUID = 1598606714;

    public static final SAuditlog auditlog = new SAuditlog("auditlog_");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final NumberPath<Long> itemId = createNumber("ITEM_ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SAuditlog> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SItem> auditlog_ITEMIDFK = createForeignKey(itemId, "ID");

    public SAuditlog(String variable) {
        super(SAuditlog.class, forVariable(variable), "null", "auditlog_");
    }

    public SAuditlog(Path<? extends SAuditlog> path) {
        super(path.getType(), path.getMetadata(), "null", "auditlog_");
    }

    public SAuditlog(PathMetadata<?> metadata) {
        super(SAuditlog.class, metadata, "null", "auditlog_");
    }

}

