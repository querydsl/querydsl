package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SName is a Querydsl query type for SName
 */
public class SName extends RelationalPathBase<SName> implements RelationalPath<SName> {

    private static final long serialVersionUID = 501063508;

    public static final SName name = new SName("NAME");

    public final StringPath firstname = createString("FIRSTNAME");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath lastname = createString("LASTNAME");

    public final StringPath nickname = createString("NICKNAME");

    public final PrimaryKey<SName> sql100819184435150 = createPrimaryKey(id);

    public final ForeignKey<SCustomer> _fk27fbe3fe4707a44 = new ForeignKey<SCustomer>(this, id, "NAME_ID");

    public SName(String variable) {
        super(SName.class, forVariable(variable), null, "NAME");
    }

    public SName(BeanPath<? extends SName> entity) {
        super(entity.getType(), entity.getMetadata(), null, "NAME");
    }

    public SName(PathMetadata<?> metadata) {
        super(SName.class, metadata, null, "NAME");
    }

}

