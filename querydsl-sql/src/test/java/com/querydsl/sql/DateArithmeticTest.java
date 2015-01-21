package com.querydsl.sql;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.path.DateTimePath;

public class DateArithmeticTest {

    private String serialize(Expression<?> expr, SQLTemplates templates) {
        SQLSerializer serializer = new SQLSerializer(new Configuration(templates));
        serializer.handle(expr);
        return serializer.toString();
    }

    @Test
    public void test() {
        List<SQLTemplates> list = Lists.newArrayList();
        list.add(new CUBRIDTemplates());
        list.add(new DerbyTemplates());
        list.add(new H2Templates());
        list.add(new HSQLDBTemplates());
        list.add(new MySQLTemplates());
        list.add(new OracleTemplates());
        list.add(new PostgresTemplates());
        list.add(new SQLiteTemplates());
        list.add(new SQLServerTemplates());
        list.add(new SQLServer2005Templates());
        list.add(new SQLServer2012Templates());
        list.add(new TeradataTemplates());

        List<Expression<?>> exprs = Lists.newArrayList();
        DateTimePath<Date> path = new DateTimePath<Date>(Date.class, "date");
        exprs.add(SQLExpressions.addYears(path, 2));
        exprs.add(SQLExpressions.addMonths(path, 2));
        exprs.add(SQLExpressions.addDays(path, 2));
        exprs.add(SQLExpressions.addHours(path, 2));
        exprs.add(SQLExpressions.addMinutes(path, 2));
        exprs.add(SQLExpressions.addSeconds(path, 2));

        for (SQLTemplates templates : list) {
            System.out.println(templates.getClass().getSimpleName());
            for (Expression<?> expr : exprs) {
                System.err.println(serialize(expr, templates));
            }
            System.out.println();

        }
    }

}
