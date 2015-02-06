package com.querydsl.core.domain.query3;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.domain.CompanyGroupPK;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.BeanPath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;


/**
 * QCompanyGroupPK is a Querydsl query type for CompanyGroupPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QTCompanyGroupPK extends BeanPath<CompanyGroupPK> {

    private static final long serialVersionUID = 1605808658;

    public static final QTCompanyGroupPK companyGroupPK = new QTCompanyGroupPK("companyGroupPK");

    public final StringPath companyNumber = createString("companyNumber");

    public final NumberPath<Long> companyType = createNumber("companyType", Long.class);

    public QTCompanyGroupPK(String variable) {
        super(CompanyGroupPK.class, forVariable(variable));
    }

    public QTCompanyGroupPK(Path<? extends CompanyGroupPK> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QTCompanyGroupPK(PathMetadata metadata) {
        super(CompanyGroupPK.class, metadata);
    }

}

