package com.querydsl.r2dbc;

import com.google.common.collect.Lists;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.testutil.ReportingOnly;
import com.querydsl.r2dbc.domain.QEmployee;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;

@Category(ReportingOnly.class)
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
        list.add(new OracleTemplates());  // inner query
        list.add(new PostgreSQLTemplates());
        list.add(new SQLiteTemplates());
        list.add(new SQLServerTemplates());
        list.add(new SQLServer2005Templates()); // inner query
        list.add(new SQLServer2012Templates());

        for (SQLTemplates templates : list) {
            QEmployee employee = QEmployee.employee;
            QueryMixin<?> query = new QueryMixin<Void>();
            query.from(employee);
            query.orderBy(employee.firstname.asc());
            query.setProjection(employee.id);

            System.out.println(templates.getClass().getSimpleName());
            System.out.println();

            // limit
            query.restrict(QueryModifiers.limit(10L));
            System.out.println("* limit");
            System.out.println(serialize(query.getMetadata(), templates));
            System.out.println();

            if (!templates.getClass().equals(SQLServerTemplates.class)) {
                // offset
                query.restrict(QueryModifiers.offset(10L));
                System.out.println("* offset");
                System.out.println(serialize(query.getMetadata(), templates));
                System.out.println();

                // limit and offset
                query.restrict(new QueryModifiers(10L, 10L));
                System.out.println("* limit and offset");
                System.out.println(serialize(query.getMetadata(), templates));
                System.out.println();
            }

        }
    }

}
