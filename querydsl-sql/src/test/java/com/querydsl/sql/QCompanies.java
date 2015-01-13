package com.querydsl.sql;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;


/**
 * QCompanies is a Querydsl querydsl type for QCompanies
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QCompanies extends com.querydsl.sql.RelationalPathBase<QCompanies> {

    private static final long serialVersionUID = 1808918375;

    public static final QCompanies companies = new QCompanies("COMPANIES");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<QCompanies> constraint5 = createPrimaryKey(id);

    public QCompanies(String variable) {
        super(QCompanies.class, forVariable(variable), "PUBLIC", "COMPANIES");
        addMetadata();
    }

    public QCompanies(Path<? extends QCompanies> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "COMPANIES");
        addMetadata();
    }

    public QCompanies(PathMetadata<?> metadata) {
        super(QCompanies.class, metadata, "PUBLIC", "COMPANIES");
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(id, ColumnMetadata.named("ID"));
        addMetadata(name, ColumnMetadata.named("NAME"));
    }

}
