package com.querydsl.core.domain.query;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import com.querydsl.core.annotations.Generated;
import com.querydsl.core.domain.CompanyPK;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.StringPath;


/**
 * QCompanyPK is a Querydsl query type for CompanyPK
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

    public QCompanyPK(PathMetadata metadata) {
        super(CompanyPK.class, metadata);
    }

}

