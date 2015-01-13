package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SKittensSet is a Querydsl querydsl type for SKittensSet
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SKittensSet extends com.querydsl.sql.RelationalPathBase<SKittensSet> {

    private static final long serialVersionUID = -227477337;

    public static final SKittensSet kittensSet = new SKittensSet("kittens_set");

    public final NumberPath<Integer> catId = createNumber("catId", Integer.class);

    public final NumberPath<Integer> kittenId = createNumber("kittenId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<SKittensSet> primary = createPrimaryKey(catId, kittenId);

    public final com.querydsl.sql.ForeignKey<SAnimal> fk4fccad6f8f00fdf8 = createForeignKey(catId, "id");

    public final com.querydsl.sql.ForeignKey<SAnimal> fk4fccad6f3881aaa7 = createForeignKey(kittenId, "id");

    public SKittensSet(String variable) {
        super(SKittensSet.class, forVariable(variable), "", "kittens_set");
        addMetadata();
    }

    public SKittensSet(String variable, String schema, String table) {
        super(SKittensSet.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SKittensSet(Path<? extends SKittensSet> path) {
        super(path.getType(), path.getMetadata(), "", "kittens_set");
        addMetadata();
    }

    public SKittensSet(PathMetadata<?> metadata) {
        super(SKittensSet.class, metadata, "", "kittens_set");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(catId, ColumnMetadata.named("cat_id").withIndex(1).ofType(4).withSize(10).notNull());
        addMetadata(kittenId, ColumnMetadata.named("kitten_id").withIndex(2).ofType(4).withSize(10).notNull());
    }

}

