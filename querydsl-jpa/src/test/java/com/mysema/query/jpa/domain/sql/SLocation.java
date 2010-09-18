package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;


/**
 * SLocation is a Querydsl query type for SLocation
 */
@Table("LOCATION")
public class SLocation extends RelationalPathBase<SLocation> {

    private static final long serialVersionUID = -1336395778;

    public static final SLocation location = new SLocation("LOCATION");

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final StringPath name = createString("NAME");

    public final PrimaryKey<SLocation> sql100819184434890 = createPrimaryKey(id);

    public final ForeignKey<SStore> _fk4c808c12adf2d04 = new ForeignKey<SStore>(this, id, "LOCATION_ID");

    public SLocation(String variable) {
        super(SLocation.class, forVariable(variable));
    }

    public SLocation(BeanPath<? extends SLocation> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SLocation(PathMetadata<?> metadata) {
        super(SLocation.class, metadata);
    }
    
}

