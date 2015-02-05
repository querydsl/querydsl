package com.querydsl.core.domain.query3;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.domain.CompanyGroup;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.PathInits;


/**
 * QCompanyGroup is a Querydsl query type for CompanyGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTCompanyGroup extends EntityPathBase<CompanyGroup> {

    private static final long serialVersionUID = 144687575;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QTCompanyGroup companyGroup = new QTCompanyGroup("companyGroup");

    public final QTCompanyGroupPK key;

    public final QTCompany mainCompany;

    public QTCompanyGroup(String variable) {
        this(CompanyGroup.class, forVariable(variable), INITS);
    }

    public QTCompanyGroup(PathMetadata metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTCompanyGroup(PathMetadata metadata, PathInits inits) {
        this(CompanyGroup.class, metadata, inits);
    }

    public QTCompanyGroup(Class<? extends CompanyGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.key = inits.isInitialized("key") ? new QTCompanyGroupPK(forProperty("key")) : null;
        this.mainCompany = inits.isInitialized("mainCompany") ? new QTCompany(forProperty("mainCompany"), inits.get("mainCompany")) : null;
    }

}

