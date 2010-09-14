package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SEmployee is a Querydsl query type for SEmployee
 */
@Table("EMPLOYEE")
public class SEmployee extends RelationalPathBase<SEmployee> {

    private static final long serialVersionUID = -2043969801;

    public static final SEmployee employee = new SEmployee("EMPLOYEE");

    public final NumberPath<Integer> companyId = createNumber("COMPANY_ID", Integer.class);

    public final StringPath firstname = createString("FIRSTNAME");

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath lastname = createString("LASTNAME");

    public final PrimaryKey<SEmployee> sql100819184433200 = createPrimaryKey(id);

    public final ForeignKey<SCompany> fk4afd4ace555fdbf0 = new ForeignKey<SCompany>(this, companyId, "ID");

    public final ForeignKey<SCompany> _fk9bdfd45d8e79ac65 = new ForeignKey<SCompany>(this, id, "CEO_ID");

    public SEmployee(String variable) {
        super(SEmployee.class, forVariable(variable));
    }

    public SEmployee(BeanPath<? extends SEmployee> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SEmployee(PathMetadata<?> metadata) {
        super(SEmployee.class, metadata);
    }

}

