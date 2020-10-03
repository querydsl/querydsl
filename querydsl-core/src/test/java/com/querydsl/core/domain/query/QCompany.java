package com.querydsl.core.domain.query;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import com.querydsl.core.annotations.Generated;
import com.querydsl.core.domain.Company;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = 616888712;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QCompany company = new QCompany("company");

    public final QCompanyPK key;

    public QCompany(String variable) {
        this(Company.class, forVariable(variable), INITS);
    }

    public QCompany(PathMetadata metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCompany(PathMetadata metadata, PathInits inits) {
        this(Company.class, metadata, inits);
    }

    public QCompany(Class<? extends Company> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.key = inits.isInitialized("key") ? new QCompanyPK(forProperty("key")) : null;
    }

}

