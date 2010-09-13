/**
 * 
 */
package com.mysema.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.Table;
import com.mysema.query.types.Expression;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.PathMetadataFactory;

@Table("GENERATED_KEYS")
public class QGeneratedKeysEntity extends BeanPath<QGeneratedKeysEntity> implements RelationalPath<QGeneratedKeysEntity>{

    private static final long serialVersionUID = 2002306246819687158L;

    public QGeneratedKeysEntity(String name) {
        super(QGeneratedKeysEntity.class, PathMetadataFactory.forVariable(name));
    }

    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

    public final StringPath name = createString("NAME");

    @Override
    public Collection<ForeignKey<?>> getForeignKeys() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ForeignKey<?>> getInverseForeignKeys() {
        return Collections.emptyList();
    }

    @Override
    public PrimaryKey<QGeneratedKeysEntity> getPrimaryKey() {
        return null;
    }

    @Override
    public List<Expression<?>> getColumns() {
        return Arrays.<Expression<?>>asList(id, name);
    }

}