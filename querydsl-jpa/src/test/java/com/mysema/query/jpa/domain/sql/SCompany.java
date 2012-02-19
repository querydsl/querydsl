package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SCompany is a Querydsl query type for SCompany
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SCompany extends com.mysema.query.sql.RelationalPathBase<SCompany> {

    private static final long serialVersionUID = -692002196;

    public static final SCompany company = new SCompany("COMPANY_");

    public final NumberPath<Integer> ceoId = createNumber("CEO_ID", Integer.class);

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public final com.mysema.query.sql.PrimaryKey<SCompany> sql120219232322520 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SEmployee> fkdc405382edf003bd = createForeignKey(ceoId, "ID");

    public final com.mysema.query.sql.ForeignKey<SEmployee> _fk9d39ef71dc953998 = createInvForeignKey(id, "COMPANY_ID");

    public final com.mysema.query.sql.ForeignKey<SUser> _fk6a68df4dc953998 = createInvForeignKey(id, "COMPANY_ID");

    public final com.mysema.query.sql.ForeignKey<SDepartment> _fk1f3a274ddc953998 = createInvForeignKey(id, "COMPANY_ID");

    public SCompany(String variable) {
        super(SCompany.class, forVariable(variable), "APP", "COMPANY_");
    }

    public SCompany(Path<? extends SCompany> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "COMPANY_");
    }

    public SCompany(PathMetadata<?> metadata) {
        super(SCompany.class, metadata, "APP", "COMPANY_");
    }

}

