package com.querydsl.core.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.*;
import com.querydsl.core.types.path.*;

import javax.annotation.Generated;


/**
 * QCompanyPK is a Querydsl querydsl type for CompanyPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QCompanyPK extends BeanPath<CompanyPK> {

    private static final long serialVersionUID = 124567939;

    public static final QCompanyPK companyPK = new QCompanyPK("companyPK");

    public final StringPath id = createString("id");

    public QCompanyPK(String variable) {
        super(CompanyPK.class, forVariable(variable));
    }

    public QCompanyPK(Path<? extends CompanyPK> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QCompanyPK(PathMetadata<?> metadata) {
        super(CompanyPK.class, metadata);
    }

}

