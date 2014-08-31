package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SKittens is a Querydsl query type for SKittens
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SKittens extends com.mysema.query.sql.RelationalPathBase<SKittens> {

    private static final long serialVersionUID = 1947872699;

    public static final SKittens kittens = new SKittens("kittens");

    public final NumberPath<Integer> catId = createNumber("catId", Integer.class);

    public final NumberPath<Integer> ind = createNumber("ind", Integer.class);

    public final NumberPath<Integer> kittenId = createNumber("kittenId", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SKittens> primary = createPrimaryKey(catId, ind);

    public final com.mysema.query.sql.ForeignKey<SAnimal> fkd60087cc3881aaa7 = createForeignKey(kittenId, "id");

    public final com.mysema.query.sql.ForeignKey<SAnimal> fkd60087cc8f00fdf8 = createForeignKey(catId, "id");

    public SKittens(String variable) {
        super(SKittens.class, forVariable(variable), "", "kittens");
        addMetadata();
    }

    public SKittens(String variable, String schema, String table) {
        super(SKittens.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SKittens(Path<? extends SKittens> path) {
        super(path.getType(), path.getMetadata(), "", "kittens");
        addMetadata();
    }

    public SKittens(PathMetadata<?> metadata) {
        super(SKittens.class, metadata, "", "kittens");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(catId, ColumnMetadata.named("cat_id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(ind, ColumnMetadata.named("ind").withIndex(3).ofType(4).withSize(10).notNull());
        addMetadata(kittenId, ColumnMetadata.named("kitten_id").withIndex(2).ofType(4).withSize(10).notNull());
    }

}

