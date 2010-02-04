package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemProcedures is a Querydsl query type for QSystemProcedures
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_PROCEDURES")
public class QSystemProcedures extends PEntity<QSystemProcedures> {

    public final PNumber<Integer> numInputParams = createNumber("NUM_INPUT_PARAMS", Integer.class);

    public final PNumber<Integer> numOutputParams = createNumber("NUM_OUTPUT_PARAMS", Integer.class);

    public final PNumber<Integer> numResultSets = createNumber("NUM_RESULT_SETS", Integer.class);

    public final PString origin = createString("ORIGIN");

    public final PString procedureCat = createString("PROCEDURE_CAT");

    public final PString procedureName = createString("PROCEDURE_NAME");

    public final PString procedureSchem = createString("PROCEDURE_SCHEM");

    public final PNumber<Short> procedureType = createNumber("PROCEDURE_TYPE", Short.class);

    public final PString remarks = createString("REMARKS");

    public final PString specificName = createString("SPECIFIC_NAME");

    public QSystemProcedures(String variable) {
        super(QSystemProcedures.class, forVariable(variable));
    }

    public QSystemProcedures(PEntity<? extends QSystemProcedures> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemProcedures(PathMetadata<?> metadata) {
        super(QSystemProcedures.class, metadata);
    }

}

