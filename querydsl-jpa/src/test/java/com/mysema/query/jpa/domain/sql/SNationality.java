package com.mysema.query.jpa.domain.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * SNationality is a Querydsl query type for SNationality
 */
@Generated("com.mysema.query.sql.MetaDataSerializer")
public class SNationality extends com.mysema.query.sql.RelationalPathBase<SNationality> {

    private static final long serialVersionUID = 292541483;

    public static final SNationality nationality = new SNationality("NATIONALITY_");

    public final NumberPath<Integer> calendarId = createNumber("CALENDAR_ID", Integer.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final com.mysema.query.sql.PrimaryKey<SNationality> sql120219232327010 = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<SCalendar> fkab8efa23591ebbc = createForeignKey(calendarId, "ID");

    public final com.mysema.query.sql.ForeignKey<SPerson> _fkd78fcfaaf6578e38 = createInvForeignKey(id, "NATIONALITY_ID");

    public SNationality(String variable) {
        super(SNationality.class, forVariable(variable), "APP", "NATIONALITY_");
    }

    public SNationality(Path<? extends SNationality> entity) {
        super(entity.getType(), entity.getMetadata(), "APP", "NATIONALITY_");
    }

    public SNationality(PathMetadata<?> metadata) {
        super(SNationality.class, metadata, "APP", "NATIONALITY_");
    }

}

