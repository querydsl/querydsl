package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SName is a Querydsl query type for SName
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SName extends com.mysema.query.sql.RelationalPathBase<SName> {

    private static final long serialVersionUID = -116118301;

    public static final SName name_ = new SName("name_");

    public final StringPath firstName = createString("firstName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lastName = createString("lastName");

    public final StringPath nickName = createString("nickName");

    public final com.mysema.query.sql.PrimaryKey<SName> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCustomer> _fk600e7c4196a83d9c = createInvForeignKey(id, "name_id");

    public SName(String variable) {
        super(SName.class, forVariable(variable), "null", "name_");
        addMetadata();
    }

    public SName(String variable, String schema, String table) {
        super(SName.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SName(Path<? extends SName> path) {
        super(path.getType(), path.getMetadata(), "null", "name_");
        addMetadata();
    }

    public SName(PathMetadata<?> metadata) {
        super(SName.class, metadata, "null", "name_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(firstName, ColumnMetadata.named("firstName").withIndex(2).ofType(12).withSize(255));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(lastName, ColumnMetadata.named("lastName").withIndex(3).ofType(12).withSize(255));
        addMetadata(nickName, ColumnMetadata.named("nickName").withIndex(4).ofType(12).withSize(255));
    }

}

