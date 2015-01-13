package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SStatus is a Querydsl querydsl type for SStatus
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SStatus extends com.querydsl.sql.RelationalPathBase<SStatus> {

    private static final long serialVersionUID = 755356828;

    public static final SStatus status_ = new SStatus("status_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<SStatus> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SItem> _fk5fde7acd23307bc = createInvForeignKey(id, "status_id");

    public final com.querydsl.sql.ForeignKey<SItem> _fk5fde7ac9ea26263 = createInvForeignKey(id, "currentStatus_id");

    public SStatus(String variable) {
        super(SStatus.class, forVariable(variable), "", "status_");
        addMetadata();
    }

    public SStatus(String variable, String schema, String table) {
        super(SStatus.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SStatus(Path<? extends SStatus> path) {
        super(path.getType(), path.getMetadata(), "", "status_");
        addMetadata();
    }

    public SStatus(PathMetadata<?> metadata) {
        super(SStatus.class, metadata, "", "status_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(12).withSize(255));
    }

}

