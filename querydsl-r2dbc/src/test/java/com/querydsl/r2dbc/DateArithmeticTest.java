package com.querydsl.r2dbc;

import com.querydsl.core.testutil.ReportingOnly;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<SQLTemplates> list = new ArrayList<>();
        list.add(new H2Templates());
        list.add(new MySQLTemplates());
        list.add(new PostgreSQLTemplates());
        list.add(new SQLServerTemplates());
        list.add(new SQLServer2005Templates());
        list.add(new SQLServer2012Templates());

        List<Expression<?>> exprs = new ArrayList<>();
        DateTimePath<LocalDateTime> path = Expressions.dateTimePath(LocalDateTime.class, "date");
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
