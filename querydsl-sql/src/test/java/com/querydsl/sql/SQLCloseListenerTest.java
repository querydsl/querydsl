package com.querydsl.sql;

import static com.querydsl.sql.Constants.employee;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.sql.domain.Employee;

public class SQLCloseListenerTest {

    private SQLQuery<Employee> query;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        Connections.initH2();
        Configuration conf = new Configuration(H2Templates.DEFAULT);
        conf.addListener(SQLCloseListener.DEFAULT);
        query = new SQLQuery<Void>(Connections.getConnection(), conf).select(employee).from(employee);
    }

    @After
    public void tearDown() throws SQLException {
        Connections.close();
    }

    @Test
    public void fetch() {
        assertFalse(query.fetch().isEmpty());
    }

    @Test
    public void fetchOne() {
        assertNotNull(query.limit(1).fetchOne());
    }

    @Test
    public void fetchFirst() {
        assertNotNull(query.fetchFirst());
    }

    @Test
    public void fetchResults() {
        assertNotNull(query.fetchResults());
    }

    @Test
    public void iterate() {
        CloseableIterator<Employee> it = query.iterate();
        try {
            while (it.hasNext()) {
                it.next();
            }
        } finally {
            it.close();
        }
    }


}
