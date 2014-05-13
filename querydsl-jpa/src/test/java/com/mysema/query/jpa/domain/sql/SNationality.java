package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * SNationality is a Querydsl query type for SNationality
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SNationality extends com.mysema.query.sql.RelationalPathBase<SNationality> {

    private static final long serialVersionUID = 478851476;

    public static final SNationality nationality_ = new SNationality("nationality_");

    public final NumberPath<Integer> calendarId = createNumber("calendarId", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SNationality> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCalendar> fkab8efa23591ebbc = createForeignKey(calendarId, "id");

    public final com.mysema.query.sql.ForeignKey<SPerson> _fkd78fcfaaf6578e38 = createInvForeignKey(id, "nationality_id");

    public SNationality(String variable) {
        super(SNationality.class, forVariable(variable), "null", "nationality_");
        addMetadata();
    }

    public SNationality(String variable, String schema, String table) {
        super(SNationality.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SNationality(Path<? extends SNationality> path) {
        super(path.getType(), path.getMetadata(), "null", "nationality_");
        addMetadata();
    }

    public SNationality(PathMetadata<?> metadata) {
        super(SNationality.class, metadata, "null", "nationality_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(calendarId, ColumnMetadata.named("calendar_id").withIndex(2).ofType(4).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

