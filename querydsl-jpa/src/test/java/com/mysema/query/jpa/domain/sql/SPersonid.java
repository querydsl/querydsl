package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SPersonid is a Querydsl query type for SPersonid
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SPersonid extends com.mysema.query.sql.RelationalPathBase<SPersonid> {

    private static final long serialVersionUID = -1323129506;

    public static final SPersonid personid_ = new SPersonid("personid_");

    public final StringPath country = createString("country");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> medicareNumber = createNumber("medicareNumber", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SPersonid> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SPerson> _fkd78fcfaad7999e61 = createInvForeignKey(id, "pid_id");

    public SPersonid(String variable) {
        super(SPersonid.class, forVariable(variable), "null", "personid_");
        addMetadata();
    }

    public SPersonid(String variable, String schema, String table) {
        super(SPersonid.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SPersonid(Path<? extends SPersonid> path) {
        super(path.getType(), path.getMetadata(), "null", "personid_");
        addMetadata();
    }

    public SPersonid(PathMetadata<?> metadata) {
        super(SPersonid.class, metadata, "null", "personid_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(country, ColumnMetadata.named("country").withIndex(2).ofType(12).withSize(255));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(medicareNumber, ColumnMetadata.named("medicareNumber").withIndex(3).ofType(4).withSize(10).notNull());
    }

}

