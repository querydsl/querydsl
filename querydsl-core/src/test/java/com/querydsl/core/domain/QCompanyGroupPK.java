package com.querydsl.core.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.*;
import com.querydsl.core.types.path.*;

import javax.annotation.Generated;


/**
 * QCompanyGroupPK is a Querydsl querydsl type for CompanyGroupPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QCompanyGroupPK extends BeanPath<CompanyGroupPK> {

    private static final long serialVersionUID = 1605808658;

    public static final QCompanyGroupPK companyGroupPK = new QCompanyGroupPK("companyGroupPK");

    public final StringPath companyNumber = createString("companyNumber");

    public final NumberPath<Long> companyType = createNumber("companyType", Long.class);

    public QCompanyGroupPK(String variable) {
        super(CompanyGroupPK.class, forVariable(variable));
    }

    public QCompanyGroupPK(Path<? extends CompanyGroupPK> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QCompanyGroupPK(PathMetadata<?> metadata) {
        super(CompanyGroupPK.class, metadata);
    }

}

