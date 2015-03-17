package com.querydsl.core.domain.query2;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.domain.CompanyPK;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.StringPath;


/**
 * QCompanyPK is a Querydsl query type for CompanyPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QCompanyPKType extends BeanPath<CompanyPK> {

    private static final long serialVersionUID = 124567939;

    public static final QCompanyPKType companyPK = new QCompanyPKType("companyPK");

    public final StringPath id = createString("id");

    public QCompanyPKType(String variable) {
        super(CompanyPK.class, forVariable(variable));
    }

    public QCompanyPKType(Path<? extends CompanyPK> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QCompanyPKType(PathMetadata metadata) {
        super(CompanyPK.class, metadata);
    }

}

