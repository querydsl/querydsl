package com.mysema.query;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.sql.domain.QSurvey;
import com.mysema.query.types.query.ListSubQuery;

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
