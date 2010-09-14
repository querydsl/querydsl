package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;


/**
 * SCatalog is a Querydsl query type for SCatalog
 */
@Table("CATALOG")
public class SCatalog extends RelationalPathBase<SCatalog> {

    private static final long serialVersionUID = 1005460144;

    public static final SCatalog catalog = new SCatalog("CATALOG");

    public final DatePath<java.sql.Date> effectivedate = createDate("EFFECTIVEDATE", java.sql.Date.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final PrimaryKey<SCatalog> sql100819184431520 = createPrimaryKey(id);

    public final ForeignKey<SCatalogPrice> _fke4eb7d63f28fe670 = new ForeignKey<SCatalogPrice>(this, id, "CATALOG_ID");

    public SCatalog(String variable) {
        super(SCatalog.class, forVariable(variable));
    }

    public SCatalog(BeanPath<? extends SCatalog> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SCatalog(PathMetadata<?> metadata) {
        super(SCatalog.class, metadata);
    }

}

