package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCompany is a Querydsl query type for SCompany
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCompany extends com.mysema.query.sql.RelationalPathBase<SCompany> {

    private static final long serialVersionUID = -692002196;

    public static final SCompany company = new SCompany("company_");

    public final NumberPath<Integer> ceoId = createNumber("ceo_id", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> ratingOrdinal = createNumber("ratingOrdinal", Integer.class);

    public final StringPath ratingString = createString("ratingString");

    public final com.mysema.query.sql.PrimaryKey<SCompany> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SEmployee> fkdc405382edf003bd = createForeignKey(ceoId, "id");

    public final com.mysema.query.sql.ForeignKey<SEmployee> company_CEOIDFK = createForeignKey(ceoId, "id");

    public final com.mysema.query.sql.ForeignKey<SEmployee> _fk9d39ef71dc953998 = createInvForeignKey(id, "company_id");

    public final com.mysema.query.sql.ForeignKey<SCompany_department> _company_department_CompanyIDFK = createInvForeignKey(id, "Company_ID");

    public final com.mysema.query.sql.ForeignKey<SEmployee> _employee_COMPANYIDFK = createInvForeignKey(id, "company_id");

    public final com.mysema.query.sql.ForeignKey<SUser> _user_COMPANYIDFK = createInvForeignKey(id, "company_id");

    public final com.mysema.query.sql.ForeignKey<SUser> _fk6a68df4dc953998 = createInvForeignKey(id, "company_id");

    public final com.mysema.query.sql.ForeignKey<SCompany_department> _fk100ba610f0d30873 = createInvForeignKey(id, "company__id");

    public final com.mysema.query.sql.ForeignKey<SDepartment> _department_COMPANYIDFK = createInvForeignKey(id, "COMPANY_ID");

    public SCompany(String variable) {
        super(SCompany.class, forVariable(variable), "null", "company_");
    }

    public SCompany(Path<? extends SCompany> path) {
        super(path.getType(), path.getMetadata(), "null", "company_");
    }

    public SCompany(PathMetadata<?> metadata) {
        super(SCompany.class, metadata, "null", "company_");
    }

}

