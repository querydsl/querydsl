package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SPersonid is a Querydsl querydsl type for SPersonid
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SPersonid extends com.querydsl.sql.RelationalPathBase<SPersonid> {

    private static final long serialVersionUID = -1323129506;

    public static final SPersonid personid_ = new SPersonid("personid_");

    public final StringPath country = createString("country");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> medicareNumber = createNumber("medicareNumber", Integer.class);

    public final com.querydsl.sql.PrimaryKey<SPersonid> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SPerson> _fkd78fcfaad7999e61 = createInvForeignKey(id, "pid_id");

    public SPersonid(String variable) {
        super(SPersonid.class, forVariable(variable), "", "personid_");
        addMetadata();
    }

    public SPersonid(String variable, String schema, String table) {
        super(SPersonid.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SPersonid(Path<? extends SPersonid> path) {
        super(path.getType(), path.getMetadata(), "", "personid_");
        addMetadata();
    }

    public SPersonid(PathMetadata<?> metadata) {
        super(SPersonid.class, metadata, "", "personid_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(country, ColumnMetadata.named("country").withIndex(2).ofType(12).withSize(255));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(medicareNumber, ColumnMetadata.named("medicareNumber").withIndex(3).ofType(4).withSize(10).notNull());
    }

}

