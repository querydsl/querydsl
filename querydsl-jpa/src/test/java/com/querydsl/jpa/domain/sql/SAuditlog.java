package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SAuditlog is a Querydsl querydsl type for SAuditlog
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SAuditlog extends com.querydsl.sql.RelationalPathBase<SAuditlog> {

    private static final long serialVersionUID = -1982799323;

    public static final SAuditlog auditlog_ = new SAuditlog("auditlog_");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Long> itemId = createNumber("itemId", Long.class);

    public final com.querydsl.sql.PrimaryKey<SAuditlog> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SItem> fkb88fbf6ae26109c = createForeignKey(itemId, "id");

    public SAuditlog(String variable) {
        super(SAuditlog.class, forVariable(variable), "", "auditlog_");
        addMetadata();
    }

    public SAuditlog(String variable, String schema, String table) {
        super(SAuditlog.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SAuditlog(Path<? extends SAuditlog> path) {
        super(path.getType(), path.getMetadata(), "", "auditlog_");
        addMetadata();
    }

    public SAuditlog(PathMetadata<?> metadata) {
        super(SAuditlog.class, metadata, "", "auditlog_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(itemId, ColumnMetadata.named("item_id").withIndex(2).ofType(-5).withSize(19));
    }

}

