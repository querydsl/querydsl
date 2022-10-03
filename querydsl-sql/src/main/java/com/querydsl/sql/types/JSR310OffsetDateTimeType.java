package com.querydsl.sql.types;

import org.jetbrains.annotations.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.OffsetDateTime;

public class JSR310OffsetDateTimeType extends AbstractJSR310DateTimeType<OffsetDateTime> {


    public JSR310OffsetDateTimeType() {
        super(Types.TIMESTAMP);
    }

    public JSR310OffsetDateTimeType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(OffsetDateTime value) {
        return dateTimeFormatter.format(value);
    }

    @Override
    public Class<OffsetDateTime> getReturnedClass() {
        return OffsetDateTime.class;
    }

    @Nullable
    @Override
    public OffsetDateTime getValue(ResultSet rs, int startIndex) throws SQLException {
        return rs.getObject(startIndex, OffsetDateTime.class);
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, OffsetDateTime value) throws SQLException {
        st.setObject(startIndex, value);
    }
}
