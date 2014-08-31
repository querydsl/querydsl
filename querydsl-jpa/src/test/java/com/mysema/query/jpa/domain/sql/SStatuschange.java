package com.mysema.query.jpa.domain.sql;

import javax.annotation.Generated;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.NumberPath;
import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SStatuschange is a Querydsl query type for SStatuschange
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SStatuschange extends com.mysema.query.sql.RelationalPathBase<SStatuschange> {

    private static final long serialVersionUID = 87971116;

    public static final SStatuschange statuschange_ = new SStatuschange("statuschange_");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.sql.Timestamp> timeStamp = createDateTime("timeStamp", java.sql.Timestamp.class);

    public final com.mysema.query.sql.PrimaryKey<SStatuschange> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SItem_statuschange> _fkcb99fb2ab2bd098d = createInvForeignKey(id, "statusChanges_id");

    public SStatuschange(String variable) {
        super(SStatuschange.class, forVariable(variable), "", "statuschange_");
        addMetadata();
    }

    public SStatuschange(String variable, String schema, String table) {
        super(SStatuschange.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SStatuschange(Path<? extends SStatuschange> path) {
        super(path.getType(), path.getMetadata(), "", "statuschange_");
        addMetadata();
    }

    public SStatuschange(PathMetadata<?> metadata) {
        super(SStatuschange.class, metadata, "", "statuschange_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
        addMetadata(timeStamp, ColumnMetadata.named("timeStamp").withIndex(2).ofType(93).withSize(19));
    }

}

