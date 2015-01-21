package com.querydsl.core.domain.query3;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.domain.Company;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.PathInits;


/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = 616888712;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QTCompany company = new QTCompany("company");

    public final QTCompanyPK key;

    public QTCompany(String variable) {
        this(Company.class, forVariable(variable), INITS);
    }

    public QTCompany(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTCompany(PathMetadata<?> metadata, PathInits inits) {
        this(Company.class, metadata, inits);
    }

    public QTCompany(Class<? extends Company> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.key = inits.isInitialized("key") ? new QTCompanyPK(forProperty("key")) : null;
    }

}

