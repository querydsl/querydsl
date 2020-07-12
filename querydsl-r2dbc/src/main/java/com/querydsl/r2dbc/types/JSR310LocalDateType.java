package com.querydsl.r2dbc.types;

import com.querydsl.r2dbc.binding.BindMarker;
import com.querydsl.r2dbc.binding.BindTarget;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

/**
 * JSR310LocalDateType maps {@linkplain LocalDate}
 * to {@linkplain Date} on the JDBC level
 */
@IgnoreJRERequirement //conditionally included
public class JSR310LocalDateType extends AbstractJSR310DateTimeType<LocalDate> {

    public JSR310LocalDateType() {
        super(Types.DATE);
    }

    public JSR310LocalDateType(int type) {
        super(type);
    }

    @Override
    public String getLiteral(LocalDate value) {
        return dateFormatter.format(value);
    }

    @Override
    public Class<LocalDate> getReturnedClass() {
        return LocalDate.class;
    }

    @Override
    public void setValue(BindMarker bindMarker, BindTarget bindTarget, LocalDate value) {
        try {
            super.setValue(bindMarker, bindTarget, value);
        } catch (Exception e) {
            bindMarker.bind(bindTarget, value);
        }
    }

    @Override
    protected LocalDate fromDbValue(Temporal value) {
        if (LocalDateTime.class.isAssignableFrom(value.getClass())) {
            return ((LocalDateTime) value).toLocalDate();
        }

        return super.fromDbValue(value);
    }
}
