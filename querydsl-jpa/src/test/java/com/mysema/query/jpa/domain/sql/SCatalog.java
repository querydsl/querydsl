package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SCatalog is a Querydsl query type for SCatalog
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCatalog extends com.mysema.query.sql.RelationalPathBase<SCatalog> {

    private static final long serialVersionUID = 669498199;

    public static final SCatalog catalog_ = new SCatalog("catalog_");

    public final DatePath<java.sql.Date> effectiveDate = createDate("effectiveDate", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SCatalog> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCatalog_price> _fkaa04532fbb9021ab = createInvForeignKey(id, "catalog__id");

    public SCatalog(String variable) {
        super(SCatalog.class, forVariable(variable), "null", "catalog_");
        addMetadata();
    }

    public SCatalog(String variable, String schema, String table) {
        super(SCatalog.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCatalog(Path<? extends SCatalog> path) {
        super(path.getType(), path.getMetadata(), "null", "catalog_");
        addMetadata();
    }

    public SCatalog(PathMetadata<?> metadata) {
        super(SCatalog.class, metadata, "null", "catalog_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(effectiveDate, ColumnMetadata.named("effectiveDate").withIndex(2).ofType(91).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
    }

}

