package com.mysema.query.jpa.domain.sql;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * SCalendar is a Querydsl query type for SCalendar
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SCalendar extends com.mysema.query.sql.RelationalPathBase<SCalendar> {

    private static final long serialVersionUID = 885543696;

    public static final SCalendar calendar_ = new SCalendar("calendar_");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<SCalendar> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCalendarHolidays> _fk31ce1edc591ebbc = createInvForeignKey(id, "Calendar_id");

    public final com.mysema.query.sql.ForeignKey<SNationality> _fkab8efa23591ebbc = createInvForeignKey(id, "calendar_id");

    public SCalendar(String variable) {
        super(SCalendar.class, forVariable(variable), "null", "calendar_");
        addMetadata();
    }

    public SCalendar(String variable, String schema, String table) {
        super(SCalendar.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCalendar(Path<? extends SCalendar> path) {
        super(path.getType(), path.getMetadata(), "null", "calendar_");
        addMetadata();
    }

    public SCalendar(PathMetadata<?> metadata) {
        super(SCalendar.class, metadata, "null", "calendar_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
    }

}

