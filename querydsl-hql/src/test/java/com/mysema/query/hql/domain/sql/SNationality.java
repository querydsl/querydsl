package com.mysema.query.hql.domain.sql;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.PrimaryKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;


/**
 * SNationality is a Querydsl query type for SNationality
 */
@Table("NATIONALITY")
public class SNationality extends RelationalPathBase<SNationality> implements RelationalPath<SNationality> {

    private static final long serialVersionUID = 1320834259;

    public static final SNationality nationality = new SNationality("NATIONALITY");

    public final NumberPath<Integer> calendarId = createNumber("CALENDAR_ID", Integer.class);

    public final NumberPath<Long> id = createNumber("ID", Long.class);

    public final PrimaryKey<SNationality> sql100819184436080 = createPrimaryKey(id);

    public final ForeignKey<SCalendar> fk68f2659ca61b9464 = new ForeignKey<SCalendar>(this, calendarId, "ID");

    public final ForeignKey<SPerson> _fk8e488775e9d94490 = new ForeignKey<SPerson>(this, id, "NATIONALITY_ID");

    public SNationality(String variable) {
        super(SNationality.class, forVariable(variable));
    }

    public SNationality(BeanPath<? extends SNationality> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public SNationality(PathMetadata<?> metadata) {
        super(SNationality.class, metadata);
    }
    
}

