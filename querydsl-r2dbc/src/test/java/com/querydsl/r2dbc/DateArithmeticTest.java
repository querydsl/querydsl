package com.querydsl.r2dbc;

import com.google.common.collect.Lists;
import com.querydsl.core.testutil.ReportingOnly;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;
import java.util.List;

@Category(ReportingOnly.class)
public class DateArithmeticTest {

    private String serialize(Expression<?> expr, SQLTemplates templates) {
        SQLSerializer serializer = new SQLSerializer(new Configuration(templates));
        serializer.handle(expr);
        return serializer.toString();
    }

    @Test
    public void test() {
        List<SQLTemplates> list = Lists.newArrayList();
        list.add(new H2Templates());
        list.add(new MySQLTemplates());
        list.add(new PostgreSQLTemplates());
        list.add(new SQLiteTemplates());
        list.add(new SQLServerTemplates());
        list.add(new SQLServer2005Templates());
        list.add(new SQLServer2012Templates());

        List<Expression<?>> exprs = Lists.newArrayList();
        DateTimePath<Date> path = Expressions.dateTimePath(Date.class, "date");
        exprs.add(R2DBCExpressions.addYears(path, 2));
        exprs.add(R2DBCExpressions.addMonths(path, 2));
        exprs.add(R2DBCExpressions.addDays(path, 2));
        exprs.add(R2DBCExpressions.addHours(path, 2));
        exprs.add(R2DBCExpressions.addMinutes(path, 2));
        exprs.add(R2DBCExpressions.addSeconds(path, 2));

        for (SQLTemplates templates : list) {
            System.out.println(templates.getClass().getSimpleName());
            for (Expression<?> expr : exprs) {
                System.err.println(serialize(expr, templates));
            }
            System.out.println();

        }
    }

}
