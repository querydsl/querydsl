package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SFooNames is a Querydsl querydsl type for SFooNames
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SFooNames extends com.querydsl.sql.RelationalPathBase<SFooNames> {

    private static final long serialVersionUID = 982089235;

    public static final SFooNames fooNames = new SFooNames("foo_names");

    public final NumberPath<Integer> fooId = createNumber("fooId", Integer.class);

    public final StringPath names = createString("names");

    public final com.querydsl.sql.ForeignKey<SFoo> fkb6129a8f94e297f8 = createForeignKey(fooId, "id");

    public SFooNames(String variable) {
        super(SFooNames.class, forVariable(variable), "", "foo_names");
        addMetadata();
    }

    public SFooNames(String variable, String schema, String table) {
        super(SFooNames.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SFooNames(Path<? extends SFooNames> path) {
        super(path.getType(), path.getMetadata(), "", "foo_names");
        addMetadata();
    }

    public SFooNames(PathMetadata<?> metadata) {
        super(SFooNames.class, metadata, "", "foo_names");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(fooId, ColumnMetadata.named("foo_id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(names, ColumnMetadata.named("names").withIndex(2).ofType(12).withSize(255));
    }

}

