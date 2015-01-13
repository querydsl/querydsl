package com.querydsl.jpa.domain.sql;

import javax.annotation.Generated;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.NumberPath;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * SNationality is a Querydsl querydsl type for SNationality
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class SNationality extends com.querydsl.sql.RelationalPathBase<SNationality> {

    private static final long serialVersionUID = 478851476;

    public static final SNationality nationality_ = new SNationality("nationality_");

    public final NumberPath<Integer> calendarId = createNumber("calendarId", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.querydsl.sql.PrimaryKey<SNationality> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SCalendar> fkab8efa23591ebbc = createForeignKey(calendarId, "id");

    public final com.querydsl.sql.ForeignKey<SPerson> _fkd78fcfaaf6578e38 = createInvForeignKey(id, "nationality_id");

    public SNationality(String variable) {
        super(SNationality.class, forVariable(variable), "", "nationality_");
        addMetadata();
    }

    public SNationality(String variable, String schema, String table) {
        super(SNationality.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public SNationality(Path<? extends SNationality> path) {
        super(path.getType(), path.getMetadata(), "", "nationality_");
        addMetadata();
    }

    public SNationality(PathMetadata<?> metadata) {
        super(SNationality.class, metadata, "", "nationality_");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(calendarId, ColumnMetadata.named("calendar_id").withIndex(2).ofType(4).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(-5).withSize(19).notNull());
    }

}

