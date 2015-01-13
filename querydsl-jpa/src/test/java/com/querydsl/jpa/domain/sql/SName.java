package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SName is a Querydsl querydsl type for SName
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SName extends com.querydsl.sql.RelationalPathBase<SName> {

    private static final long serialVersionUID = -116118301;

    public static final SName name_ = new SName("name_");

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lastName = createString("lastName");

    public final StringPath nickName = createString("nickName");

    public final com.querydsl.sql.PrimaryKey<SName> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SCustomer> _fk600e7c4196a83d9c = createInvForeignKey(id, "name_id");

    public SName(String variable) {
        super(SName.class, forVariable(variable), "", "name_");
        addMetadata();
    }

    public SName(String variable, String schema, String table) {
        super(SName.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SName(Path<? extends SName> path) {
        super(path.getType(), path.getMetadata(), "", "name_");
        addMetadata();
    }

    public SName(PathMetadata<?> metadata) {
        super(SName.class, metadata, "", "name_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(firstName, ColumnMetadata.named("firstName").withIndex(2).ofType(12).withSize(255));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(lastName, ColumnMetadata.named("lastName").withIndex(3).ofType(12).withSize(255));
        addMetadata(nickName, ColumnMetadata.named("nickName").withIndex(4).ofType(12).withSize(255));
    }

}

