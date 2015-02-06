package com.querydsl.core.domain.query3;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.domain.CompanyPK;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.BeanPath;
import com.querydsl.core.types.path.StringPath;


/**
 * QCompanyPK is a Querydsl query type for CompanyPK
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QTCompanyPK extends BeanPath<CompanyPK> {

    private static final long serialVersionUID = 124567939;

    public static final QTCompanyPK companyPK = new QTCompanyPK("companyPK");

    public final StringPath id = createString("id");

    public QTCompanyPK(String variable) {
        super(CompanyPK.class, forVariable(variable));
    }

    public QTCompanyPK(Path<? extends CompanyPK> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QTCompanyPK(PathMetadata metadata) {
        super(CompanyPK.class, metadata);
    }

}

