/**
 * 
 */
package com.mysema.query;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

//@Table("GENERATED_KEYS")
public class QGeneratedKeysEntity extends RelationalPathBase<QGeneratedKeysEntity>{

    private static final long serialVersionUID = 2002306246819687158L;

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    public QGeneratedKeysEntity(String name) {
        super(QGeneratedKeysEntity.class, PathMetadataFactory.forVariable(name), null, "GENERATED_KEYS");
    }

}