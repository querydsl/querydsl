package com.querydsl.r2dbc;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.PrimaryKey;
import com.querydsl.sql.RelationalPathBase;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QCompanies is a Querydsl query type for QCompanies
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QCompanies extends RelationalPathBase<QCompanies> {

    private static final long serialVersionUID = 1808918375;

    public static final QCompanies companies = new QCompanies("COMPANIES");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final PrimaryKey<QCompanies> constraint5 = createPrimaryKey(id);

    public QCompanies(String variable) {
        super(QCompanies.class, forVariable(variable), "PUBLIC", "COMPANIES");
        addMetadata();
    }

    public QCompanies(Path<? extends QCompanies> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "COMPANIES");
        addMetadata();
    }

    public QCompanies(PathMetadata metadata) {
        super(QCompanies.class, metadata, "PUBLIC", "COMPANIES");
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(id, ColumnMetadata.named("ID"));
        addMetadata(name, ColumnMetadata.named("NAME"));
    }

}
