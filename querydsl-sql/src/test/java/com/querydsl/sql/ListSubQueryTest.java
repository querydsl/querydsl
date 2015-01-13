package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLSubQuery;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.sql.domain.QSurvey;
import com.querydsl.core.types.query.ListSubQuery;

public class ListSubQueryTest {

    @Test
    public void HashCode() {
        QSurvey survey = QSurvey.survey;
        QSurvey survey2 = new QSurvey("survey2");
        ListSubQuery<Tuple> query1 = new SQLSubQuery().from(survey).list(survey.all());
        ListSubQuery<Tuple> query2 = new SQLSubQuery().from(survey2).list(survey2.all());


        Set<ListSubQuery<Tuple>> queries = new HashSet<ListSubQuery<Tuple>>();
        queries.add(query1);
        queries.add(query2);
        assertEquals(2, queries.size());
    }
    
    @Test
    public void HashCode2() {
        QSurvey survey = new QSurvey("entity");
        QEmployee employee = new QEmployee("entity");
        ListSubQuery<Integer> query1 = new SQLSubQuery().from(survey).list(survey.id);
        ListSubQuery<Integer> query2 = new SQLSubQuery().from(employee).list(employee.id);

        Set<ListSubQuery<Integer>> queries = new HashSet<ListSubQuery<Integer>>();
        queries.add(query1);
        queries.add(query2);
        assertEquals(1, queries.size());
    }
}
