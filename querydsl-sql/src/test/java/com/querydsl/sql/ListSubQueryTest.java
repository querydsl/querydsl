package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.sql.domain.QSurvey;

public class ListSubQueryTest {

    @Test
    public void HashCode() {
        QSurvey survey = QSurvey.survey;
        QSurvey survey2 = new QSurvey("survey2");
        SubQueryExpression<Tuple> query1 = new SQLQuery<Void>().from(survey).select(survey.all());
        SubQueryExpression<Tuple> query2 = new SQLQuery<Void>().from(survey2).select(survey2.all());


        Set<SubQueryExpression<Tuple>> queries = Sets.newHashSet();
        queries.add(query1);
        queries.add(query2);
        assertEquals(2, queries.size());
    }
    
    @Test
    public void HashCode2() {
        QSurvey survey = new QSurvey("entity");
        QEmployee employee = new QEmployee("entity");
        SubQueryExpression<Integer> query1 = new SQLQuery<Void>().from(survey).select(survey.id);
        SubQueryExpression<Integer> query2 = new SQLQuery<Void>().from(employee).select(employee.id);

        Set<SubQueryExpression<Integer>> queries = Sets.newHashSet();
        queries.add(query1);
        queries.add(query2);
        assertEquals(1, queries.size());
    }
}
