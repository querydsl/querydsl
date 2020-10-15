package com.querydsl.core.domain.query2;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.domain.Company;
import com.querydsl.core.domain.QCompanyPK;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCompanyType extends EntityPathBase<Company> {

    private static final long serialVersionUID = 616888712;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QCompanyType company = new QCompanyType("company");

    public final QCompanyPK key;

    public QCompanyType(String variable) {
        this(Company.class, forVariable(variable), INITS);
    }

    public QCompanyType(PathMetadata metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCompanyType(PathMetadata metadata, PathInits inits) {
        this(Company.class, metadata, inits);
    }

    public QCompanyType(Class<? extends Company> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.key = inits.isInitialized("key") ? new QCompanyPK(forProperty("key")) : null;
    }

}

