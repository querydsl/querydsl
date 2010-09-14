package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SKittens is a Querydsl query type for SKittens
 */
@Table("KITTENS")
public class SKittens extends RelationalPathBase<SKittens> {

    private static final long serialVersionUID = -254852509;

    public static final SKittens kittens = new SKittens("KITTENS");

    public final NumberPath<Integer> animalId = createNumber("ANIMAL_ID", Integer.class);

    public final NumberPath<Integer> ind = createNumber("IND", Integer.class);

    public final NumberPath<Integer> kittensId = createNumber("KITTENS_ID", Integer.class);

    public final PrimaryKey<SKittens> sql100819184440200 = createPrimaryKey(animalId, ind);

    public final ForeignKey<SAnimal> fkd60087cca295046a = new ForeignKey<SAnimal>(this, animalId, "ID");

    public final ForeignKey<SAnimal> fkd60087cc7a9f89a = new ForeignKey<SAnimal>(this, kittensId, "ID");

    public SKittens(String variable) {
        super(SKittens.class, forVariable(variable));
    }

    public SKittens(BeanPath<? extends SKittens> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SKittens(PathMetadata<?> metadata) {
        super(SKittens.class, metadata);
    }

}

