package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SCalendar is a Querydsl querydsl type for SCalendar
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SCalendar extends com.querydsl.sql.RelationalPathBase<SCalendar> {

    private static final long serialVersionUID = 885543696;

    public static final SCalendar calendar_ = new SCalendar("calendar_");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final com.querydsl.sql.PrimaryKey<SCalendar> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SCalendarHolidays> _fk31ce1edc591ebbc = createInvForeignKey(id, "Calendar_id");

    public final com.querydsl.sql.ForeignKey<SNationality> _fkab8efa23591ebbc = createInvForeignKey(id, "calendar_id");

    public SCalendar(String variable) {
        super(SCalendar.class, forVariable(variable), "", "calendar_");
        addMetadata();
    }

    public SCalendar(String variable, String schema, String table) {
        super(SCalendar.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SCalendar(Path<? extends SCalendar> path) {
        super(path.getType(), path.getMetadata(), "", "calendar_");
        addMetadata();
    }

    public SCalendar(PathMetadata<?> metadata) {
        super(SCalendar.class, metadata, "", "calendar_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(4).withSize(10).notNull());
    }

}

