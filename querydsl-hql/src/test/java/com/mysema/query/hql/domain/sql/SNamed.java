package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SNamed is a Querydsl query type for SNamed
 */
@Table("NAMED")
public class SNamed extends RelationalPathBase<SNamed> implements RelationalPath<SNamed> {

    private static final long serialVersionUID = -1646900336;

    public static final SNamed named = new SNamed("NAMED");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final PrimaryKey<SNamed> sql100819184435830 = createPrimaryKey(id);

    public SNamed(String variable) {
        super(SNamed.class, forVariable(variable));
    }

    public SNamed(BeanPath<? extends SNamed> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SNamed(PathMetadata<?> metadata) {
        super(SNamed.class, metadata);
    }

}

