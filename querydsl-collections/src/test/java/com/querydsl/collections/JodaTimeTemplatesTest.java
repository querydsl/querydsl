package com.querydsl.collections;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.TimePath;

public class JodaTimeTemplatesTest {

    private CollQuery<?> query = new CollQuery<Void>(JodaTimeTemplates.DEFAULT);

    @Test
    public void dateTime() {
        DateTimePath<DateTime> entity = Expressions.dateTimePath(DateTime.class, "entity");
        query.from(entity, Arrays.asList(new DateTime(), new DateTime(0L)))
             .select(entity.year(), entity.yearMonth(), entity.month(), entity.week(),
                   entity.dayOfMonth(), entity.dayOfWeek(), entity.dayOfYear(),
                   entity.hour(), entity.minute(), entity.second(), entity.milliSecond())
             .fetch();
    }

    @Test
    public void localDate() {
        DatePath<LocalDate> entity = Expressions.datePath(LocalDate.class, "entity");
        query.from(entity, Arrays.asList(new LocalDate(), new LocalDate(0L)))
             .select(entity.year(), entity.yearMonth(), entity.month(), entity.week(),
                   entity.dayOfMonth(), entity.dayOfWeek(), entity.dayOfYear())
             .fetch();
    }

    @Test
    public void localTime() {
        TimePath<LocalTime> entity = Expressions.timePath(LocalTime.class, "entity");
        query.from(entity, Arrays.asList(new LocalTime(), new LocalTime(0L)))
             .select(entity.hour(), entity.minute(), entity.second(), entity.milliSecond())
             .fetch();
    }

}
