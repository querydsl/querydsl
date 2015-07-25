package com.querydsl.example.sql.repository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.runner.RunWith;

import com.querydsl.example.sql.guice.GuiceTestRunner;
import com.querydsl.example.sql.guice.Transactional;

@RunWith(GuiceTestRunner.class)
public abstract class AbstractPersistenceTest {
    @Inject
    private DataSource dataSource;

    @Before
    @Transactional
    public void before() {
        try (Connection connection = dataSource.getConnection()) {
            List<String> tables = new ArrayList<String>();
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, null,
                    new String[] { "TABLE" });
            try {
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }
            } finally {
                rs.close();
            }

            java.sql.Statement stmt = connection.createStatement();
            try {
                stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
                for (String table : tables) {
                    stmt.execute("TRUNCATE TABLE " + table);
                }
                stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
            } finally {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
