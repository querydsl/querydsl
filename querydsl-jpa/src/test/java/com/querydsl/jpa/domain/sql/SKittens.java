package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SKittens is a Querydsl querydsl type for SKittens
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SKittens extends com.querydsl.sql.RelationalPathBase<SKittens> {

    private static final long serialVersionUID = 1947872699;

    public static final SKittens kittens = new SKittens("kittens");

    public final NumberPath<Integer> catId = createNumber("catId", Integer.class);

    public final NumberPath<Integer> ind = createNumber("ind", Integer.class);

    public final NumberPath<Integer> kittenId = createNumber("kittenId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<SKittens> primary = createPrimaryKey(catId, ind);

    public final com.querydsl.sql.ForeignKey<SAnimal> fkd60087cc3881aaa7 = createForeignKey(kittenId, "id");

    public final com.querydsl.sql.ForeignKey<SAnimal> fkd60087cc8f00fdf8 = createForeignKey(catId, "id");

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

