package com.querydsl.core.domain.query2;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import com.querydsl.core.annotations.Generated;
import com.querydsl.core.domain.CompanyGroupPK;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;


/**
 * QCompanyGroupPK is a Querydsl query type for CompanyGroupPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QCompanyGroupPKType extends BeanPath<CompanyGroupPK> {

    private static final long serialVersionUID = 1605808658;

    public static final QCompanyGroupPKType companyGroupPK = new QCompanyGroupPKType("companyGroupPK");

    public final StringPath companyNumber = createString("companyNumber");

    public final NumberPath<Long> companyType = createNumber("companyType", Long.class);

    public QCompanyGroupPKType(String variable) {
        super(CompanyGroupPK.class, forVariable(variable));
    }

    public QCompanyGroupPKType(Path<? extends CompanyGroupPK> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QCompanyGroupPKType(PathMetadata metadata) {
        super(CompanyGroupPK.class, metadata);
    }

}

