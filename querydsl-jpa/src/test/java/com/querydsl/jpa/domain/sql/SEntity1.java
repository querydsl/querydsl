package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SEntity1 is a Querydsl querydsl type for SEntity1
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SEntity1 extends com.querydsl.sql.RelationalPathBase<SEntity1> {

    private static final long serialVersionUID = 1060650653;

    public static final SEntity1 Entity1 = new SEntity1("Entity1");

    public final StringPath dtype = createString("dtype");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath property = createString("property");

    public final StringPath property2 = createString("property2");

    public final com.querydsl.sql.PrimaryKey<SEntity1> primary = createPrimaryKey(id);

    public SEntity1(String variable) {
        super(SEntity1.class, forVariable(variable), "", "Entity1");
        addMetadata();
    }

    public SEntity1(String variable, String schema, String table) {
        super(SEntity1.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SEntity1(Path<? extends SEntity1> path) {
        super(path.getType(), path.getMetadata(), "", "Entity1");
        addMetadata();
    }

    public SEntity1(PathMetadata<?> metadata) {
        super(SEntity1.class, metadata, "", "Entity1");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(dtype, ColumnMetadata.named("DTYPE").withIndex(1).ofType(12).withSize(31).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(2).ofType(4).withSize(10).notNull());
        addMetadata(property, ColumnMetadata.named("property").withIndex(3).ofType(12).withSize(255));
        addMetadata(property2, ColumnMetadata.named("property2").withIndex(4).ofType(12).withSize(255));
    }

}

