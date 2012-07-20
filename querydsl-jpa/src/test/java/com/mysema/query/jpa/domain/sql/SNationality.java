package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SNationality is a Querydsl query type for SNationality
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class SNationality extends com.mysema.query.sql.RelationalPathBase<SNationality> {

    private static final long serialVersionUID = 292541483;

    public static final SNationality nationality = new SNationality("nationality_");

    public final NumberPath<Integer> calendarId = createNumber("CALENDAR_ID", Integer.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SNationality> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCalendar> nationality_CALENDARIDFK = createForeignKey(calendarId, "id");

    public final com.mysema.query.sql.ForeignKey<SPerson> _person_NATIONALITYIDFK = createInvForeignKey(id, "NATIONALITY_ID");

    public SNationality(String variable) {
        super(SNationality.class, forVariable(variable), "null", "nationality_");
    }

    public SNationality(Path<? extends SNationality> path) {
        super(path.getType(), path.getMetadata(), "null", "nationality_");
    }

    public SNationality(PathMetadata<?> metadata) {
        super(SNationality.class, metadata, "null", "nationality_");
    }

}

