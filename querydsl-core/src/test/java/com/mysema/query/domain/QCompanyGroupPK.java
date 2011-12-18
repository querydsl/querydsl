package com.mysema.query.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QCompanyGroupPK is a Querydsl query type for CompanyGroupPK
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
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

