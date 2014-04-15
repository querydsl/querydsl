package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SFooNames is a Querydsl query type for SFooNames
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SFooNames extends com.mysema.query.sql.RelationalPathBase<SFooNames> {

    private static final long serialVersionUID = 982089235;

    public static final SFooNames fooNames = new SFooNames("foo_names");

    public final NumberPath<Integer> fooId = createNumber("fooId", Integer.class);

    public final StringPath names = createString("names");

    public final com.mysema.query.sql.ForeignKey<SFoo> fkb6129a8f94e297f8 = createForeignKey(fooId, "id");

    public SFooNames(String variable) {
        super(SFooNames.class, forVariable(variable), "null", "foo_names");
        addMetadata();
    }

    public SFooNames(String variable, String schema, String table) {
        super(SFooNames.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SFooNames(Path<? extends SFooNames> path) {
        super(path.getType(), path.getMetadata(), "null", "foo_names");
        addMetadata();
    }

    public SFooNames(PathMetadata<?> metadata) {
        super(SFooNames.class, metadata, "null", "foo_names");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(fooId, ColumnMetadata.named("foo_id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(names, ColumnMetadata.named("names").withIndex(2).ofType(12).withSize(255));
    }

}

