package com.querydsl.jpa;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.querydsl.core.types.dsl.PathBuilder;

public class OrderHelperTest {

    @Test
    public void order() {
        PathBuilder<Object> entity = new PathBuilder<Object>(Object.class, "project");
        List<String> order = new ArrayList<>();
        order.add("customer.name");
        order.add("department.superior.name");
        order.add("customer.company.name");
        order.add("previousProject.customer.company.name");
        order.add("department.name");

        JPQLQuery<?> query = select(entity);
        query.from(entity);
        OrderHelper.orderBy(query, entity, order);
        assertEquals("select project\n" +
            "from Object project\n" +
            "  left join project.customer as customer\n" +
            "  left join project.department as department\n" +
            "  left join department.superior as department_superior\n" +
            "  left join customer.company as customer_company\n" +
            "  left join project.previousProject as previousProject\n" +
            "  left join previousProject.customer as previousProject_customer\n" +
            "  left join previousProject_customer.company as previousProject_customer_company\n" +
            "order by customer.name asc, department_superior.name asc, customer_company.name asc," +
                " previousProject_customer_company.name asc, department.name asc",
            query.toString());
    }

}
