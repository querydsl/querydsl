package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SStoreCustomer is a Querydsl query type for SStoreCustomer
 */
@Table("STORE_CUSTOMER")
public class SStoreCustomer extends RelationalPathBase<SStoreCustomer> implements RelationalPath<SStoreCustomer> {

    private static final long serialVersionUID = 1667012918;

    public static final SStoreCustomer storeCustomer = new SStoreCustomer("STORE_CUSTOMER");

    public final NumberPath<Integer> customersId = createNumber("CUSTOMERS_ID", Integer.class);

    public final NumberPath<Long> storeId = createNumber("STORE_ID", Long.class);

    public final ForeignKey<SStore> fk808055bc828daef0 = new ForeignKey<SStore>(this, storeId, "ID");

    public final ForeignKey<SCustomer> fk808055bcf27d6c8d = new ForeignKey<SCustomer>(this, customersId, "ID");

    public SStoreCustomer(String variable) {
        super(SStoreCustomer.class, forVariable(variable));
    }

    public SStoreCustomer(BeanPath<? extends SStoreCustomer> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SStoreCustomer(PathMetadata<?> metadata) {
        super(SStoreCustomer.class, metadata);
    }

}

