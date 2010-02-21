package com.mysema.query.hql.domain.sql;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;
import com.mysema.query.types.expr.*;
import com.mysema.query.types.custom.*;

/**
 * SStoreCustomer is a Querydsl query type for SStoreCustomer
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="STORE_CUSTOMER")
public class SStoreCustomer extends PEntity<SStoreCustomer> {

    public final PNumber<Integer> customersId = createNumber("CUSTOMERS_ID", Integer.class);

    public final PNumber<Long> storeId = createNumber("STORE_ID", Long.class);

    public SStoreCustomer(String variable) {
        super(SStoreCustomer.class, forVariable(variable));
    }

    public SStoreCustomer(PEntity<? extends SStoreCustomer> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SStoreCustomer(PathMetadata<?> metadata) {
        super(SStoreCustomer.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

