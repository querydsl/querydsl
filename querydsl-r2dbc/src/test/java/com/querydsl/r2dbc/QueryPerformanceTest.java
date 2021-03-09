package com.querydsl.r2dbc;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.testutil.H2;
import com.querydsl.core.testutil.Performance;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

@Category({H2.class, Performance.class})
@Ignore("currently R2DBC has known READ performance issues - also there is a bug in the tests somewhere")
public class QueryPerformanceTest {

    private static final String QUERY = "select COMPANIES.NAME\n" +
            "from COMPANIES COMPANIES\n" +
            "where COMPANIES.ID = ?";

    private static final SQLTemplates templates = new H2Templates();

    private static final Configuration conf = new Configuration(templates);

    @BeforeClass
    public static void setUpClass() {
        Connections.initH2();
        Connection conn = Connections.getConnection();
        Statement stmt = conn.createStatement("create or replace table companies (id identity, name varchar(30) unique not null);");
        Mono.from(stmt.execute()).block();

        Statement pstmt = conn.createStatement("insert into companies (name) values (?)");
        final int iterations = 1000000;
        for (int i = 0; i < iterations; i++) {
            pstmt.bind(1, String.valueOf(i));
            Mono.from(pstmt.execute()).block();
        }

        conn.setAutoCommit(false);
    }

    @AfterClass
    public static void tearDownClass() {
        Connection conn = Connections.getConnection();
        Statement stmt = conn.createStatement("drop table companies");
        Mono.from(stmt.execute()).block();
    }

//    @Test
//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    @OutputTimeUnit(TimeUnit.MICROSECONDS)
//    public void jDBC() throws Exception {
//        Connection conn = Connections.getH2().getConnection().block();
//        PreparedStatement stmt = conn.prepareStatement(QUERY)){
//            stmt.setLong(1, ThreadLocalRandom.current().nextLong());
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    rs.getString(1);
//                }
//            }
//
//        }
//    }

//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    @OutputTimeUnit(TimeUnit.MICROSECONDS)
//    public void jDBC2() throws Exception {
//        try (java.sql.Connection conn = Connections.getH2();
//             PreparedStatement stmt = conn.prepareStatement(QUERY)) {
//            stmt.setString(1, String.valueOf(ThreadLocalRandom.current().nextLong()));
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    rs.getString(1);
//                }
//            }
//
//        }
//    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl1() throws Exception {
        QCompanies companies = QCompanies.companies;
        R2DBCQuery<?> query = new R2DBCQuery<Void>(Connections.getConnection(), conf, new DefaultQueryMetadata());
        query.from(companies).where(companies.id.eq((long) ThreadLocalRandom.current().nextLong()))
                .select(companies.name).fetch().collectList().block();
    }

//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    @OutputTimeUnit(TimeUnit.MICROSECONDS)
//    public void querydsl12() throws Exception {
//        try (java.sql.Connection conn = Connections.getH2()) {
//            QCompanies companies = QCompanies.companies;
//            SQLQuery<?> query = new SQLQuery<Void>(conn, conf);
//            try (CloseableIterator<String> it = query.from(companies)
//                    .where(companies.id.eq((long) ThreadLocalRandom.current().nextLong())).select(companies.name).iterate()) {
//                while (it.hasNext()) {
//                    it.next();
//                }
//            }
//        }
//    }
//
//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    @OutputTimeUnit(TimeUnit.MICROSECONDS)
//    public void querydsl13() throws Exception {
//        try (java.sql.Connection conn = Connections.getH2()) {
//            QCompanies companies = QCompanies.companies;
//            SQLQuery<?> query = new SQLQuery<Void>(conn, conf);
//            try (ResultSet rs = query.select(companies.name).from(companies)
//                    .where(companies.id.eq((long) ThreadLocalRandom.current().nextLong())).getResults()) {
//                while (rs.next()) {
//                    rs.getString(1);
//                }
//            }
//        }
//    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl14() throws Exception {
        QCompanies companies = QCompanies.companies;
        R2DBCQuery<?> query = new R2DBCQuery<Void>(Connections.getConnection(), conf, new DefaultQueryMetadata());
        query.from(companies).where(companies.id.eq((long) ThreadLocalRandom.current().nextLong()))
                .select(companies.name).fetch().collectList().block();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl15() throws Exception {
        QCompanies companies = QCompanies.companies;
        R2DBCQuery<?> query = new R2DBCQuery<Void>(Connections.getConnection(), conf, new DefaultQueryMetadata());
        query.from(companies).where(companies.id.eq((long) ThreadLocalRandom.current().nextLong()))
                .select(companies.id, companies.name).fetch().collectList().block();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl2() throws Exception {
        QCompanies companies = QCompanies.companies;
        R2DBCQuery<?> query = new R2DBCQuery<Void>(Connections.getConnection(), conf);
        query.from(companies).where(companies.name.eq(String.valueOf(ThreadLocalRandom.current().nextLong())))
                .select(companies.name).fetch().collectList().block();
    }

//    @Benchmark
//    @BenchmarkMode(Mode.AverageTime)
//    @OutputTimeUnit(TimeUnit.MICROSECONDS)
//    public void querydsl22() throws Exception {
//        QCompanies companies = QCompanies.companies;
//        R2DBCQuery<?> query = new R2DBCQuery<Void>(conn, conf);
//        try (CloseableIterator<String> it = query.from(companies)
//                .where(companies.name.eq(String.valueOf(ThreadLocalRandom.current().nextLong())))
//                .select(companies.name).fe()) {
//            while (it.hasNext()) {
//                it.next();
//            }
//        }
//    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void querydsl23() throws Exception {
        QCompanies companies = QCompanies.companies;
        R2DBCQuery<?> query = new R2DBCQuery<Void>(Connections.getConnection(), conf, new DefaultQueryMetadata());

        query
                .from(companies)
                .where(companies.name.eq(String.valueOf(ThreadLocalRandom.current().nextLong())))
                .select(companies.name)
                .fetch()
                .collectList()
                .block();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void serialization() throws Exception {
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
