package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SCustomer is a Querydsl query type for SCustomer
 */
public class SCustomer extends RelationalPathBase<SCustomer> {

    private static final long serialVersionUID = 1663703079;

    public static final SCustomer customer = new SCustomer("CUSTOMER");

    public final NumberPath<Long> currentorderId = createNumber("CURRENTORDER_ID", Long.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final NumberPath<Long> nameId = createNumber("NAME_ID", Long.class);

    public final PrimaryKey<SCustomer> sql100819184432460 = createPrimaryKey(id);

    public final ForeignKey<SOrder> fk27fbe3fee803b049 = new ForeignKey<SOrder>(this, currentorderId, "ID");

    public final ForeignKey<SName> fk27fbe3fe4707a44 = new ForeignKey<SName>(this, nameId, "ID");

    public final ForeignKey<SStoreCustomer> _fk808055bcf27d6c8d = new ForeignKey<SStoreCustomer>(this, id, "CUSTOMERS_ID");

    public final ForeignKey<SOrder> _fk8b7256f15325d064 = new ForeignKey<SOrder>(this, id, "CUSTOMER_ID");

    public SCustomer(String variable) {
        super(SCustomer.class, forVariable(variable), null, "CUSTOMER");
    }

    public SCustomer(BeanPath<? extends SCustomer> entity) {
        super(entity.getType(), entity.getMetadata(), null, "CUSTOMER");
    }

    public SCustomer(PathMetadata<?> metadata) {
        super(SCustomer.class, metadata, null, "CUSTOMER");
    }

}

