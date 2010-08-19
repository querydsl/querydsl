package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SStoreCustomer is a Querydsl query type for SStoreCustomer
 */
@Table("STORE_CUSTOMER")
public class SStoreCustomer extends BeanPath<SStoreCustomer> implements RelationalPath<SStoreCustomer> {

    private static final long serialVersionUID = 1667012918;

    public static final SStoreCustomer storeCustomer = new SStoreCustomer("STORE_CUSTOMER");

    public final PNumber<Integer> customersId = createNumber("CUSTOMERS_ID", Integer.class);

    public final PNumber<Long> storeId = createNumber("STORE_ID", Long.class);

    private Expr[] _all;

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

    public Expr[] all() {
        if (_all == null) {
            _all = new Expr[]{customersId, storeId};
        }
        return _all;
    }

    public PrimaryKey<SStoreCustomer> getPrimaryKey() {
        return null;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk808055bc828daef0, fk808055bcf27d6c8d);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.<ForeignKey<?>>emptyList();
    }

}

