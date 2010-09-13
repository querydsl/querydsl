package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import com.mysema.query.sql.*;
import java.util.*;


/**
 * SCustomer is a Querydsl query type for SCustomer
 */
@Table("CUSTOMER")
public class SCustomer extends BeanPath<SCustomer> implements RelationalPath<SCustomer> {

    private static final long serialVersionUID = 1663703079;

    public static final SCustomer customer = new SCustomer("CUSTOMER");

    public final NumberPath<Long> currentorderId = createNumber("CURRENTORDER_ID", Long.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final NumberPath<Long> nameId = createNumber("NAME_ID", Long.class);

    private Expression[] _all;

    public final PrimaryKey<SCustomer> sql100819184432460 = new PrimaryKey<SCustomer>(this, id);

    public final ForeignKey<SOrder> fk27fbe3fee803b049 = new ForeignKey<SOrder>(this, currentorderId, "ID");

    public final ForeignKey<SName> fk27fbe3fe4707a44 = new ForeignKey<SName>(this, nameId, "ID");

    public final ForeignKey<SStoreCustomer> _fk808055bcf27d6c8d = new ForeignKey<SStoreCustomer>(this, id, "CUSTOMERS_ID");

    public final ForeignKey<SOrder> _fk8b7256f15325d064 = new ForeignKey<SOrder>(this, id, "CUSTOMER_ID");

    public SCustomer(String variable) {
        super(SCustomer.class, forVariable(variable));
    }

    public SCustomer(BeanPath<? extends SCustomer> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SCustomer(PathMetadata<?> metadata) {
        super(SCustomer.class, metadata);
    }

    public Expression[] all() {
        if (_all == null) {
            _all = new Expression[]{currentorderId, id, nameId};
        }
        return _all;
    }

    public PrimaryKey<SCustomer> getPrimaryKey() {
        return sql100819184432460;
    }

    public List<ForeignKey<?>> getForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(fk27fbe3fee803b049, fk27fbe3fe4707a44);
    }

    public List<ForeignKey<?>> getInverseForeignKeys() {
        return Arrays.<ForeignKey<?>>asList(_fk808055bcf27d6c8d, _fk8b7256f15325d064);
    }

    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(all());
    }
}

