package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SStore is a Querydsl query type for SStore
 */
@Table("STORE")
public class SStore extends RelationalPathBase<SStore> implements RelationalPath<SStore> {

    private static final long serialVersionUID = -1641714376;

    public static final SStore store = new SStore("STORE");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final NumberPath<Long> locationId = createNumber("LOCATION_ID", Long.class);

    public final PrimaryKey<SStore> sql100819184439380 = createPrimaryKey(id);

    public final ForeignKey<SLocation> fk4c808c12adf2d04 = new ForeignKey<SLocation>(this, locationId, "ID");

    public final ForeignKey<SStoreCustomer> _fk808055bc828daef0 = new ForeignKey<SStoreCustomer>(this, id, "STORE_ID");

    public SStore(String variable) {
        super(SStore.class, forVariable(variable));
    }

    public SStore(BeanPath<? extends SStore> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SStore(PathMetadata<?> metadata) {
        super(SStore.class, metadata);
    }

}

