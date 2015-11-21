package com.querydsl.sql;

import static com.querydsl.sql.SQLExpressions.select;
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
    public void hashCode1() {
        QSurvey survey = QSurvey.survey;
        QSurvey survey2 = new QSurvey("survey2");
        SubQueryExpression<Tuple> query1 = select(survey.all()).from(survey);
        SubQueryExpression<Tuple> query2 = select(survey2.all()).from(survey2);

        Set<SubQueryExpression<Tuple>> queries = Sets.newHashSet();
        queries.add(query1);
        queries.add(query2);
        assertEquals(2, queries.size());
    }

    @Test
    public void hashCode2() {
        QSurvey survey = new QSurvey("entity");
        QEmployee employee = new QEmployee("entity");
        SubQueryExpression<Integer> query1 = select(survey.id).from(survey);
        SubQueryExpression<Integer> query2 = select(employee.id).from(employee);

        Set<SubQueryExpression<Integer>> queries = Sets.newHashSet();
        queries.add(query1);
        queries.add(query2);
        assertEquals(1, queries.size());
    }
}
