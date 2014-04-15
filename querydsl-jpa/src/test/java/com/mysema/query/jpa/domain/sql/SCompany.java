package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SCompany is a Querydsl query type for SCompany
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCompany extends com.mysema.query.sql.RelationalPathBase<SCompany> {

    private static final long serialVersionUID = 22768499;

    public static final SCompany company_ = new SCompany("company_");

    public final NumberPath<Integer> ceoId = createNumber("ceoId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> ratingOrdinal = createNumber("ratingOrdinal", Integer.class);

    public final StringPath ratingString = createString("ratingString");

    public final com.mysema.query.sql.PrimaryKey<SCompany> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SEmployee> fkdc405382edf003bd = createForeignKey(ceoId, "id");

    public final com.mysema.query.sql.ForeignKey<SEmployee> _fk9d39ef71dc953998 = createInvForeignKey(id, "company_id");

    public final com.mysema.query.sql.ForeignKey<SUser> _fk6a68df4dc953998 = createInvForeignKey(id, "company_id");

    public final com.mysema.query.sql.ForeignKey<SCompany_department> _fk100ba610f0d30873 = createInvForeignKey(id, "company__id");

    public final com.mysema.query.sql.ForeignKey<SDepartment> _fk1f3a274ddc953998 = createInvForeignKey(id, "company_id");

    public SCompany(String variable) {
        super(SCompany.class, forVariable(variable), "null", "company_");
        addMetadata();
    }

    public SCompany(String variable, String schema, String table) {
        super(SCompany.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCompany(Path<? extends SCompany> path) {
        super(path.getType(), path.getMetadata(), "null", "company_");
        addMetadata();
    }

    public SCompany(PathMetadata<?> metadata) {
        super(SCompany.class, metadata, "null", "company_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(ceoId, ColumnMetadata.named("ceo_id").withIndex(5).ofType(4).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
        addMetadata(ratingOrdinal, ColumnMetadata.named("ratingOrdinal").withIndex(3).ofType(4).withSize(10));
        addMetadata(ratingString, ColumnMetadata.named("ratingString").withIndex(4).ofType(12).withSize(255));
    }

}

