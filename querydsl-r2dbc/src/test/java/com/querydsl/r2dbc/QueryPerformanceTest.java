package com.querydsl.r2dbc;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.testutil.Benchmark;
import com.querydsl.core.testutil.H2;
import com.querydsl.core.testutil.Performance;
import com.querydsl.core.testutil.Runner;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import reactor.core.publisher.Mono;

import static org.junit.Assert.assertNotNull;

@Category({H2.class, Performance.class})
public class QueryPerformanceTest {

    private static final String QUERY = "select COMPANIES.NAME\n" +
            "from COMPANIES COMPANIES\n" +
            "where COMPANIES.ID = ?";

    private static final SQLTemplates templates = new H2Templates();

    private static final Configuration conf = new Configuration(templates);

    private final Connection conn = Connections.getConnection();

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


    @Test
    public void querydsl1() throws Exception {
        Runner.run("qdsl by id", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {
                    QCompanies companies = QCompanies.companies;
                    R2DBCQuery<?> query = new R2DBCQuery<Void>(conn, conf);
                    query.from(companies).where(companies.id.eq((long) i))
                            .select(companies.name).fetch().collectList().block();
                }
            }
        });
    }

//    @Test
//    public void querydsl12() throws Exception {
//        Runner.run("qdsl by id (iterated)", new Benchmark() {
//            @Override
//            public void run(int times) throws Exception {
//                for (int i = 0; i < times; i++) {
//                    QCompanies companies = QCompanies.companies;
//                    R2DBCQuery<?> query = new R2DBCQuery<Void>(conn, conf);
//                    CloseableIterator<String> it = query.from(companies)
//                            .where(companies.id.eq((long) i)).select(companies.name).iterate();
//                    try {
//                        while (it.hasNext()) {
//                            it.next();
//                        }
//                    } finally {
//                        it.close();
//                    }
//                }
//            }
//        });
//    }
//
//    @Test
//    public void querydsl13() throws Exception {
//        Runner.run("qdsl by id (result set access)", new Benchmark() {
//            @Override
//            public void run(int times) throws Exception {
//                for (int i = 0; i < times; i++) {
//                    QCompanies companies = QCompanies.companies;
//                    R2DBCQuery<?> query = new R2DBCQuery<Void>(conn, conf);
//                    ResultSet rs = query.select(companies.name).from(companies)
//                            .where(companies.id.eq((long) i)).getResults();
//                    try {
//                        while (rs.next()) {
//                            rs.getString(1);
//                        }
//                    } finally {
//                        rs.close();
//                    }
//                }
//            }
//        });
//    }

    @Test
    public void querydsl14() throws Exception {
        Runner.run("qdsl by id (no validation)", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {
                    QCompanies companies = QCompanies.companies;
                    R2DBCQuery<?> query = new R2DBCQuery<Void>(conn, conf, new DefaultQueryMetadata());
                    query.from(companies).where(companies.id.eq((long) i))
                            .select(companies.name).fetch().collectList().block();
                }
            }
        });
    }

    @Test
    public void querydsl15() throws Exception {
        Runner.run("qdsl by id (two cols)", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {
                    QCompanies companies = QCompanies.companies;
                    R2DBCQuery<?> query = new R2DBCQuery<Void>(conn, conf);
                    query.from(companies).where(companies.id.eq((long) i))
                            .select(companies.id, companies.name).fetch().collectList().block();
                }
            }
        });
    }

    @Test
    public void querydsl2() throws Exception {
        Runner.run("qdsl by name", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {
                    QCompanies companies = QCompanies.companies;
                    R2DBCQuery<?> query = new R2DBCQuery<Void>(conn, conf);
                    query.from(companies).where(companies.name.eq(String.valueOf(i)))
                            .select(companies.name).fetch().collectList().block();
                }
            }
        });
    }

//    @Test
//    public void querydsl22() throws Exception {
//        Runner.run("qdsl by name (iterated)", new Benchmark() {
//            @Override
//            public void run(int times) throws Exception {
//                for (int i = 0; i < times; i++) {
//                    QCompanies companies = QCompanies.companies;
//                    R2DBCQuery<?> query = new R2DBCQuery<Void>(conn, conf);
//                    CloseableIterator<String> it = query.from(companies)
//                            .where(companies.name.eq(String.valueOf(i)))
//                            .select(companies.name).iterate();
//                    try {
//                        while (it.hasNext()) {
//                            it.next();
//                        }
//                    } finally {
//                        it.close();
//                    }
//                }
//            }
//        });
//    }

    @Test
    public void querydsl23() throws Exception {
        Runner.run("qdsl by name (no validation)", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {
                    QCompanies companies = QCompanies.companies;
                    R2DBCQuery<?> query = new R2DBCQuery<Void>(conn, conf, new DefaultQueryMetadata());
                    query.from(companies)
                            .where(companies.name.eq(String.valueOf(i)))
                            .select(companies.name).fetch().collectList().block();
                }
            }
        });
    }

    @Test
    public void serialization() throws Exception {
        QCompanies companies = QCompanies.companies;
        final QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, companies);
        md.addWhere(companies.id.eq(1L));
        md.setProjection(companies.name);

        Runner.run("ser1", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {
                    SQLSerializer serializer = new SQLSerializer(conf);
                    serializer.serialize(md, false);
                    serializer.getConstants();
                    serializer.getConstantPaths();
                    assertNotNull(serializer.toString());
                }
            }
        });
    }
}
