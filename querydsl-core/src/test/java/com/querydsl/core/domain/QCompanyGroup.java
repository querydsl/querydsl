package com.querydsl.core.domain;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompanyGroup is a Querydsl query type for CompanyGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCompanyGroup extends EntityPathBase<CompanyGroup> {

    private static final long serialVersionUID = 144687575;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QCompanyGroup companyGroup = new QCompanyGroup("companyGroup");

    public final QCompanyGroupPK key;

    public final QCompany mainCompany;

    public QCompanyGroup(String variable) {
        this(CompanyGroup.class, forVariable(variable), INITS);
    }

    public QCompanyGroup(PathMetadata metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCompanyGroup(PathMetadata metadata, PathInits inits) {
        this(CompanyGroup.class, metadata, inits);
    }

    public QCompanyGroup(Class<? extends CompanyGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.key = inits.isInitialized("key") ? new QCompanyGroupPK(forProperty("key")) : null;
        this.mainCompany = inits.isInitialized("mainCompany") ? new QCompany(forProperty("mainCompany"), inits.get("mainCompany")) : null;
    }

}

