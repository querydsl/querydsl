package com.querydsl.sql;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.core.support.QueryMixin;

public class PaginationTest {

    private String serialize(QueryMetadata metadata, SQLTemplates templates) {
        SQLSerializer serializer = new SQLSerializer(new Configuration(templates));
        serializer.serialize(metadata, false);
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
        list.add(new OracleTemplates());  // inner querydsl
        list.add(new PostgresTemplates());
        list.add(new SQLiteTemplates());
        list.add(new SQLServerTemplates());
        list.add(new SQLServer2005Templates()); // inner querydsl
        list.add(new SQLServer2012Templates());
        list.add(new TeradataTemplates()); // qualify

        for (SQLTemplates templates : list) {
            QEmployee employee = QEmployee.employee;
            QueryMixin query = new QueryMixin();
            query.from(employee);
            query.orderBy(employee.firstname.asc());
            query.addProjection(employee.id);

            System.out.println(templates.getClass().getSimpleName());
            System.out.println();

            // limit
            query.restrict(QueryModifiers.limit(10l));
            System.out.println("* limit");
            System.out.println(serialize(query.getMetadata(), templates));
            System.out.println();

            if (!templates.getClass().equals(SQLServerTemplates.class)) {
                // offset
                query.restrict(QueryModifiers.offset(10l));
                System.out.println("* offset");
                System.out.println(serialize(query.getMetadata(), templates));
                System.out.println();

                // limit and offset
                query.restrict(new QueryModifiers(10l, 10l));
                System.out.println("* limit and offset");
                System.out.println(serialize(query.getMetadata(), templates));
                System.out.println();
            }

        }
    }

}
