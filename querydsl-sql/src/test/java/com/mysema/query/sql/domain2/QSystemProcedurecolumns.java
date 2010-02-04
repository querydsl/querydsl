package com.mysema.query.sql.domain2;

import com.mysema.query.types.path.*;
import static com.mysema.query.types.path.PathMetadataFactory.*;

/**
 * QSystemProcedurecolumns is a Querydsl query type for QSystemProcedurecolumns
 */
@SuppressWarnings("serial")
@com.mysema.query.sql.Table(value="SYSTEM_PROCEDURECOLUMNS")
public class QSystemProcedurecolumns extends PEntity<QSystemProcedurecolumns> {

    public final PString columnName = createString("COLUMN_NAME");

    public final PNumber<Short> columnType = createNumber("COLUMN_TYPE", Short.class);

    public final PNumber<Short> dataType = createNumber("DATA_TYPE", Short.class);

    public final PNumber<Integer> length = createNumber("LENGTH", Integer.class);

    public final PNumber<Short> nullable = createNumber("NULLABLE", Short.class);

    public final PNumber<Integer> precision = createNumber("PRECISION", Integer.class);

    public final PString procedureCat = createString("PROCEDURE_CAT");

    public final PString procedureName = createString("PROCEDURE_NAME");

    public final PString procedureSchem = createString("PROCEDURE_SCHEM");

    public final PNumber<Short> radix = createNumber("RADIX", Short.class);

    public final PString remarks = createString("REMARKS");

    public final PNumber<Short> scale = createNumber("SCALE", Short.class);

    public final PNumber<Integer> seq = createNumber("SEQ", Integer.class);

    public final PString specificName = createString("SPECIFIC_NAME");

    public final PString typeName = createString("TYPE_NAME");

    public QSystemProcedurecolumns(String variable) {
        super(QSystemProcedurecolumns.class, forVariable(variable));
    }

    public QSystemProcedurecolumns(PEntity<? extends QSystemProcedurecolumns> entity) {
        super(entity.getType(),entity.getMetadata());
    }

    public QSystemProcedurecolumns(PathMetadata<?> metadata) {
        super(QSystemProcedurecolumns.class, metadata);
    }

}

