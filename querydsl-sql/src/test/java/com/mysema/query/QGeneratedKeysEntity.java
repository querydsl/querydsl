/**
 * 
 */
package com.mysema.query;

import com.mysema.query.sql.Table;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

@Table("GENERATED_KEYS")
public class QGeneratedKeysEntity extends PEntity<QGeneratedKeysEntity>{

    private static final long serialVersionUID = 2002306246819687158L;

    public QGeneratedKeysEntity(String name) {
        super(QGeneratedKeysEntity.class, PathMetadataFactory.forVariable(name));
    }

    public final PNumber<java.lang.Integer> id = createNumber("ID", java.lang.Integer.class);

    public final PString name = createString("NAME");

}