package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadata;

/**
 * SEmployee is a Querydsl query type for SEmployee
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="EMPLOYEE")
public class SEmployee extends PEntity<SEmployee> {

    public final PNumber<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final PString firstname = createString("FIRSTNAME");

    public final PNumber<Integer> id = createNumber("ID", Integer.class);

    public final PString lastname = createString("LASTNAME");

    public SEmployee(String variable) {
        super(SEmployee.class, forVariable(variable));
    }

    public SEmployee(PEntity<? extends SEmployee> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public SEmployee(PathMetadata<?> metadata) {
        super(SEmployee.class, metadata);
    }

    public Expr<Object[]> all() {
        return CSimple.create(Object[].class, "{0}.*", this);
    }

}

