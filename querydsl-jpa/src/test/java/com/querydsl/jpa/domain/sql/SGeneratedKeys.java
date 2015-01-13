package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SGeneratedKeys is a Querydsl querydsl type for SGeneratedKeys
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SGeneratedKeys extends com.querydsl.sql.RelationalPathBase<SGeneratedKeys> {

    private static final long serialVersionUID = 379851474;

    public static final SGeneratedKeys generatedKeys = new SGeneratedKeys("GENERATED_KEYS");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<SGeneratedKeys> primary = createPrimaryKey(id);

    public SGeneratedKeys(String variable) {
        super(SGeneratedKeys.class, forVariable(variable), "", "GENERATED_KEYS");
        addMetadata();
    }

    public SGeneratedKeys(String variable, String schema, String table) {
        super(SGeneratedKeys.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SGeneratedKeys(Path<? extends SGeneratedKeys> path) {
        super(path.getType(), path.getMetadata(), "", "GENERATED_KEYS");
        addMetadata();
    }

    public SGeneratedKeys(PathMetadata<?> metadata) {
        super(SGeneratedKeys.class, metadata, "", "GENERATED_KEYS");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("ID").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("NAME").withIndex(2).ofType(12).withSize(30));
    }

}

