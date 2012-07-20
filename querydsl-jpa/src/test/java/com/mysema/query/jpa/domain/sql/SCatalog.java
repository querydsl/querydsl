package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCatalog is a Querydsl query type for SCatalog
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCatalog extends com.mysema.query.sql.RelationalPathBase<SCatalog> {

    private static final long serialVersionUID = -1086781944;

    public static final SCatalog catalog = new SCatalog("catalog_");

    public final DatePath<java.sql.Date> effectivedate = createDate("EFFECTIVEDATE", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SCatalog> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCatalog_price> _fkaa04532fbb9021ab = createInvForeignKey(id, "catalog__id");

    public final com.mysema.query.sql.ForeignKey<SCatalog_price> _catalog_price_CatalogIDFK = createInvForeignKey(id, "Catalog_ID");

    public SCatalog(String variable) {
        super(SCatalog.class, forVariable(variable), "null", "catalog_");
    }

    public SCatalog(Path<? extends SCatalog> path) {
        super(path.getType(), path.getMetadata(), "null", "catalog_");
    }

    public SCatalog(PathMetadata<?> metadata) {
        super(SCatalog.class, metadata, "null", "catalog_");
    }

}

