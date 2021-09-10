package com.querydsl.sql;

import com.querydsl.core.CloseableIterator;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.testutil.H2;
import com.querydsl.core.testutil.Performance;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

@Category({H2.class, Performance.class})
public class QueryPerformanceTest {

    private static final String QUERY = "select COMPANIES.NAME\n" +
            "from COMPANIES COMPANIES\n" +
            "where COMPANIES.ID = ?";

    private static final SQLTemplates templates = new H2Templates();

    private static final Configuration conf = new Configuration(templates);

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException {
        Connections.initH2();
        Connection conn = Connections.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("create or replace table companies (id identity, name varchar(30) unique not null);");

        PreparedStatement pstmt = conn.prepareStatement("insert into companies (name) values (?)");
        final int iterations = 1000000;
        for (int i = 0; i < iterations; i++) {
            pstmt.setString(1, String.valueOf(i));
            pstmt.execute();
            pstmt.clearParameters();
        }
        pstmt.close();
        stmt.close();

        conn.setAutoCommit(false);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        Connection conn = Connections.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("drop table companies");
        stmt.close();
        Connections.close();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void jDBC() throws Exception {
        try (Connection conn = Connections.getH2();
             PreparedStatement stmt = conn.prepareStatement(QUERY)) {
            stmt.setLong(1, ThreadLocalRandom.current().nextLong());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rs.getString(1);
                }
            }

        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void jDBC2() throws Exception {
        try (Connection conn = Connections.getH2();
             PreparedStatement stmt = conn.prepareStatement(QUERY)) {
            stmt.setString(1, String.valueOf(ThreadLocalRandom.current().nextLong()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rs.getString(1);
                }
            }

        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl1() throws Exception {
        try (Connection conn = Connections.getH2()) {
            QCompanies companies = QCompanies.companies;
            SQLQuery<?> query = new SQLQuery<Void>(conn, conf);
            query.from(companies).where(companies.id.eq((long) ThreadLocalRandom.current().nextLong()))
                    .select(companies.name).fetch();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl12() throws Exception {
        try (Connection conn = Connections.getH2()) {
            QCompanies companies = QCompanies.companies;
            SQLQuery<?> query = new SQLQuery<Void>(conn, conf);
            try (CloseableIterator<String> it = query.from(companies)
                    .where(companies.id.eq((long) ThreadLocalRandom.current().nextLong())).select(companies.name).iterate()) {
                while (it.hasNext()) {
                    it.next();
                }
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl13() throws Exception {
        try (Connection conn = Connections.getH2()) {
            QCompanies companies = QCompanies.companies;
            SQLQuery<?> query = new SQLQuery<Void>(conn, conf);
            try (ResultSet rs = query.select(companies.name).from(companies)
                    .where(companies.id.eq((long) ThreadLocalRandom.current().nextLong())).getResults()) {
                while (rs.next()) {
                    rs.getString(1);
                }
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl14() throws Exception {
        try (Connection conn = Connections.getH2()) {
            QCompanies companies = QCompanies.companies;
            SQLQuery<?> query = new SQLQuery<Void>(conn, conf, new DefaultQueryMetadata());
            query.from(companies).where(companies.id.eq((long) ThreadLocalRandom.current().nextLong()))
                    .select(companies.name).fetch();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl15() throws Exception {
        try (Connection conn = Connections.getH2()) {
            QCompanies companies = QCompanies.companies;
            SQLQuery<?> query = new SQLQuery<Void>(conn, conf);
            query.from(companies).where(companies.id.eq((long) ThreadLocalRandom.current().nextLong()))
                    .select(companies.id, companies.name).fetch();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl2() throws Exception {
        try (Connection conn = Connections.getH2()) {
            QCompanies companies = QCompanies.companies;
            SQLQuery<?> query = new SQLQuery<Void>(conn, conf);
            query.from(companies).where(companies.name.eq(String.valueOf(ThreadLocalRandom.current().nextLong())))
                    .select(companies.name).fetch();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl22() throws Exception {
        try (Connection conn = Connections.getH2()) {
            QCompanies companies = QCompanies.companies;
            SQLQuery<?> query = new SQLQuery<Void>(conn, conf);
            try (CloseableIterator<String> it = query.from(companies)
                    .where(companies.name.eq(String.valueOf(ThreadLocalRandom.current().nextLong())))
                    .select(companies.name).iterate()) {
                while (it.hasNext()) {
                    it.next();
                }
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl23() throws Exception {
        try (Connection conn = Connections.getH2()) {
            QCompanies companies = QCompanies.companies;
            SQLQuery<?> query = new SQLQuery<Void>(conn, conf, new DefaultQueryMetadata());
            query.from(companies)
                    .where(companies.name.eq(String.valueOf(ThreadLocalRandom.current().nextLong())))
                    .select(companies.name).fetch();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void serialization() throws Exception {
        try (Connection conn = Connections.getH2()) {
            QCompanies companies = QCompanies.companies;
            final QueryMetadata md = new DefaultQueryMetadata();
            md.addJoin(JoinType.DEFAULT, companies);
            md.addWhere(companies.id.eq(1L));
            md.setProjection(companies.name);

            SQLSerializer serializer = new SQLSerializer(conf);
            serializer.serialize(md, false);
            serializer.getConstants();
            serializer.getConstantPaths();
            assertNotNull(serializer.toString());
        }
    }

    @Test
    public void launchBenchmark() throws Exception {
        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .mode(Mode.AverageTime)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupTime(TimeValue.seconds(1))
                .warmupIterations(1)
                .measurementTime(TimeValue.seconds(1))
                .measurementIterations(3)
                .threads(1)
                .forks(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();

        new Runner(opt).run();
    }

}
