/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql;

import java.sql.*;
import java.util.Map;
import java.util.Properties;

import org.hsqldb.types.Types;

import com.google.common.collect.Maps;
import com.querydsl.core.Target;
import com.querydsl.sql.ddl.CreateTableClause;
import com.querydsl.sql.ddl.DropTableClause;

/**
 * @author tiwe
 *
 */
public final class Connections {

    public static final int TEST_ROW_COUNT = 100;

    private static ThreadLocal<Connection> connHolder = new ThreadLocal<Connection>();

    private static ThreadLocal<Target> targetHolder = new ThreadLocal<Target>();

    private static ThreadLocal<Configuration> configurationHolder = new ThreadLocal<Configuration>();

    // datetest
    private static final String CREATE_TABLE_DATETEST = "create table DATE_TEST(DATE_TEST date)";

    // survey
    private static final String CREATE_TABLE_SURVEY =
            "create table SURVEY(ID int auto_increment, NAME varchar(30), NAME2 varchar(30))";

    // test
    private static final String CREATE_TABLE_TEST = "create table TEST(NAME varchar(255))";

    // timetest
    private static final String CREATE_TABLE_TIMETEST = "create table TIME_TEST(TIME_TEST time)";

    // employee
    private static final String INSERT_INTO_EMPLOYEE = "insert into EMPLOYEE " +
            "(ID, FIRSTNAME, LASTNAME, SALARY, DATEFIELD, TIMEFIELD, SUPERIOR_ID) " +
            "values (?,?,?,?,?,?,?)";

    private static final String INSERT_INTO_TEST_VALUES = "insert into TEST values(?)";

    private static ThreadLocal<Statement> stmtHolder = new ThreadLocal<Statement>();

    private static boolean db2Inited, derbyInited, sqlServerInited, h2Inited, hsqlInited, mysqlInited, cubridInited, oracleInited, postgresqlInited, sqliteInited, teradataInited, firebirdInited;

    public static void close() throws SQLException {
        if (stmtHolder.get() != null) {
            stmtHolder.get().close();
        }
        if (connHolder.get() != null) {
            connHolder.get().close();
        }
    }

    public static Connection getConnection() {
        return connHolder.get();
    }

    public static Target getTarget() {
        return targetHolder.get();
    }

    public static Configuration getConfiguration() {
        return configurationHolder.get();
    }

    public static void initConfiguration(SQLTemplates templates) {
        configurationHolder.set(new Configuration(templates));
    }

    private static Connection getDB2() throws SQLException, ClassNotFoundException {
        Class.forName("com.ibm.db2.jcc.DB2Driver");
        String url = "jdbc:db2://db2host:50001/SAMPLE";
        return DriverManager.getConnection(url, "db2inst1", "a3sd!fDj");
    }

    private static Connection getDerby() throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        String url = "jdbc:derby:target/demoDB;create=true";
        return DriverManager.getConnection(url, "", "");
    }

    private static Connection getFirebird() throws SQLException, ClassNotFoundException {
        Class.forName("org.firebirdsql.jdbc.FBDriver");
        String url = "jdbc:firebirdsql:localhost/3050:/var/lib/firebird/2.5/data/querydsl.fdb";
        return DriverManager.getConnection(url, "querydsl", "querydsl");
    }

    private static Connection getHSQL() throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:target/tutorial";
        return DriverManager.getConnection(url, "sa", "");
    }

    private static Connection getH2() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:./target/h2-test;LOCK_MODE=0";
        return DriverManager.getConnection(url, "sa", "");
    }

    private static Connection getMySQL() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/querydsl?useLegacyDatetimeCode=false";
        return DriverManager.getConnection(url, "querydsl", "querydsl");
    }

    private static Connection getOracle() throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        return DriverManager.getConnection(url, "querydsl", "querydsl");
    }

    private static Connection getPostgreSQL() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/querydsl";
        return DriverManager.getConnection(url, "querydsl", "querydsl");
    }

    private static Connection getSQLServer() throws ClassNotFoundException, SQLException {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        Properties props = new Properties();
        props.put("user", "querydsl");
        props.put("password", "querydsl");
        props.put("sendTimeAsDatetime", "false");
        String url = "jdbc:jtds:sqlserver://localhost:1433/querydsl";
//        return DriverManager.getConnection(url, "querydsl", "querydsl");
        return DriverManager.getConnection(url, props);
    }

    private static Connection getCubrid() throws ClassNotFoundException, SQLException {
        Class.forName("cubrid.jdbc.driver.CUBRIDDriver");
        String url = "jdbc:cubrid:localhost:30000:demodb:public::";
        return DriverManager.getConnection(url);
    }

    private static Connection getSQLite() throws SQLException, ClassNotFoundException {
        //System.setProperty("sqlite.purejava", "true");
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:target/sample.db");
    }

    private static Connection getTeradata() throws SQLException, ClassNotFoundException {
        Class.forName("com.teradata.jdbc.TeraDriver");
        return DriverManager.getConnection("jdbc:teradata://teradata/dbc", "querydsl", "querydsl");
    }

    private static CreateTableClause createTable(SQLTemplates templates, String table) {
        return new CreateTableClause(connHolder.get(), new Configuration(templates), table);
    }

    public static void dropTable(SQLTemplates templates, String table) throws SQLException {
        new DropTableClause(connHolder.get(), new Configuration(templates), table).execute();
    }

    public static void dropType(Statement stmt, String type) throws SQLException {
        try {
            stmt.execute("drop type " + type);
        } catch (SQLException e) {
            if (!e.getMessage().contains("does not exist")) {
                throw e;
            }
        }
    }

    public static Statement getStatement() {
        return stmtHolder.get();
    }

    private static void createEmployeeTable(SQLTemplates templates) {
        createTable(templates, "EMPLOYEE")
                .column("ID", Integer.class).notNull()
                .column("FIRSTNAME", String.class).size(50)
                .column("LASTNAME", String.class).size(50)
                .column("SALARY", Double.class)
                .column("DATEFIELD", Date.class)
                .column("TIMEFIELD", Time.class)
                .column("SUPERIOR_ID", Integer.class)
                .primaryKey("PK_EMPLOYEE", "ID")
                .foreignKey("FK_SUPERIOR","SUPERIOR_ID").references("EMPLOYEE","ID")
                .execute();
    }

    public static Map<Integer, String> getSpatialData() {
        Map<Integer, String> m = Maps.newHashMap();
        // point
        m.put(1, "POINT (2 2)");
        m.put(2, "POINT (8 7)");
        m.put(3, "POINT (1 9)");
        m.put(4, "POINT (9 2)");
        m.put(5, "POINT (4 4)");
        // linestring
        m.put(6, "LINESTRING (30 10, 10 30)");
        m.put(7, "LINESTRING (30 10, 10 30, 40 40)");
        // polygon
        m.put(8, "POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10), (20 30, 35 35, 30 20, 20 30))");
        m.put(9, "POLYGON ((35 10, 45 45, 15 40, 10 20, 35 10), (20 30, 35 35, 30 20, 20 30))");
        // multipoint
        m.put(11, "MULTIPOINT (10 40, 40 30)");
        m.put(11, "MULTIPOINT (10 40, 40 30, 20 20, 30 10)");
        // multilinestring
        m.put(12, "MULTILINESTRING ((10 10, 20 20, 10 40), (40 40, 30 30, 40 20, 30 10))");
        m.put(13, "MULTILINESTRING ((10 10, 20 20, 10 40))");
        // multipolygon
        m.put(14, "MULTIPOLYGON (((30 20, 45 40, 10 40, 30 20)), ((15 5, 40 10, 10 20, 5 10, 15 5)))");
        m.put(15, "MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)), " +
                "((20 35, 10 30, 10 10, 30 5, 45 20, 20 35), " +
                "(30 20, 20 15, 20 25, 30 20)))");

        // XXX POLYHEDRALSURFACE not supported

        /* GEOMETRYCOLLECTION(POINT(4 6),LINESTRING(4 6,7 10))
           CIRCULARSTRING(1 5, 6 2, 7 3)
           COMPOUNDCURVE(CIRCULARSTRING(0 0,1 1,1 0),(1 0,0 1))
           CURVEPOLYGON(CIRCULARSTRING(-2 0,-1 -1,0 0,1 -1,2 0,0 2,-2 0),(-1 0,0 0.5,1 0,0 1,-1 0))
           MULTICURVE((5 5,3 5,3 3,0 3),CIRCULARSTRING(0 0,2 1,2 2))
           TRIANGLE((0 0 0,0 1 0,1 1 0,0 0 0))
           TIN (((0 0 0, 0 0 1, 0 1 0, 0 0 0)), ((0 0 0, 0 1 0, 1 1 0, 0 0 0)))
        */
        return m;
    }

    public static void initCubrid() throws SQLException, ClassNotFoundException {
        targetHolder.set(Target.CUBRID);
        //SQLTemplates templates = new MySQLTemplates();
        Connection c = getCubrid();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        if (cubridInited) {
            return;
        }

        // survey
        stmt.execute("drop table if exists SURVEY");
        stmt.execute("create table SURVEY(ID int auto_increment(16693,2), " +
                "NAME varchar(30)," +
                "NAME2 varchar(30)," +
                "constraint suryey_pk primary key(ID))");
        stmt.execute("insert into SURVEY values (1,'Hello World','Hello');");

        // test
        stmt.execute("drop table if exists \"TEST\"");
        stmt.execute("create table \"TEST\"(NAME varchar(255))");
        PreparedStatement pstmt = c.prepareStatement("insert into \"TEST\" values(?)");
        try {
            for (int i = 0; i < TEST_ROW_COUNT; i++) {
                pstmt.setString(1, "name" + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            pstmt.close();
        }

        // employee
        stmt.execute("drop table if exists EMPLOYEE");
        //createEmployeeTable(templates);
        stmt.execute("create table EMPLOYEE ( " +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "FIRSTNAME VARCHAR(50), " +
                "LASTNAME VARCHAR(50), " +
                "SALARY DECIMAL, "  +
                "DATEFIELD DATE, " +
                "TIMEFIELD TIME, " +
                "SUPERIOR_ID INT, " +
                "CONSTRAINT FK_SUPERIOR FOREIGN KEY(SUPERIOR_ID) REFERENCES EMPLOYEE(ID) " +
                ")");

        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        stmt.execute("drop table if exists TIME_TEST");
        stmt.execute("drop table if exists DATE_TEST");
        stmt.execute(CREATE_TABLE_TIMETEST);
        stmt.execute(CREATE_TABLE_DATETEST);

        // numbers
        stmt.execute("drop table if exists NUMBER_TEST");
        stmt.execute("create table NUMBER_TEST(col1 int)");

        // xml
        stmt.execute("drop table if exists XML_TEST");
        stmt.execute("create table XML_TEST(COL varchar(128))");

        cubridInited = true;
    }

    public static void initDB2() throws SQLException, ClassNotFoundException {
        targetHolder.set(Target.DB2);
        SQLTemplates templates = new DB2Templates();
        Connection c = getDB2();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        if (db2Inited) {
            return;
        }

        // survey
        dropTable(templates, "SURVEY");
        stmt.execute("create table SURVEY(" +
                "ID int generated by default as identity(start with 1, increment by 1), " +
                "NAME varchar(30)," +
                "NAME2 varchar(30))");
        stmt.execute("insert into SURVEY values (1,'Hello World','Hello')");

        // test
        dropTable(templates, "TEST");
        stmt.execute(CREATE_TABLE_TEST);
        stmt.execute("create index test_name on test(name)");
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
        try {
            for (int i = 0; i < TEST_ROW_COUNT; i++) {
                pstmt.setString(1, "name" + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            pstmt.close();
        }

        // employee
        dropTable(templates, "EMPLOYEE");

        createEmployeeTable(templates);

        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        dropTable(templates, "TIME_TEST");
        stmt.execute(CREATE_TABLE_TIMETEST);

        dropTable(templates, "DATE_TEST");
        stmt.execute(CREATE_TABLE_DATETEST);

        // numbers
        dropTable(templates, "NUMBER_TEST");
        stmt.execute("create table NUMBER_TEST(col1 smallint)");

        // xml
        dropTable(templates, "XML_TEST");
        stmt.execute("create table XML_TEST(COL varchar(128))");

        db2Inited = true;
    }


    public static void initDerby() throws SQLException, ClassNotFoundException {
        targetHolder.set(Target.DERBY);
        SQLTemplates templates = new DerbyTemplates();
        Connection c = getDerby();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        if (derbyInited) {
            return;
        }

        // types
        dropType(stmt, "price restrict");
        stmt.execute("create type price external name 'com.example.Price' language java");

        // survey
        dropTable(templates, "SURVEY");
        stmt.execute("create table SURVEY(" +
                "ID int generated by default as identity(start with 1, increment by 1), " +
                "NAME varchar(30)," +
                "NAME2 varchar(30))");
        stmt.execute("insert into SURVEY values (1,'Hello World','Hello')");

        // test
        dropTable(templates, "TEST");
        stmt.execute(CREATE_TABLE_TEST);
        stmt.execute("create index test_name on test(name)");
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
        try {
            for (int i = 0; i < TEST_ROW_COUNT; i++) {
                pstmt.setString(1, "name" + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            pstmt.close();
        }

        // employee
        dropTable(templates, "EMPLOYEE");

        createEmployeeTable(templates);

        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        dropTable(templates, "TIME_TEST");
        stmt.execute(CREATE_TABLE_TIMETEST);

        dropTable(templates, "DATE_TEST");
        stmt.execute(CREATE_TABLE_DATETEST);

        // numbers
        dropTable(templates, "NUMBER_TEST");
        stmt.execute("create table NUMBER_TEST(col1 boolean)");

        // xml
        dropTable(templates, "XML_TEST");
        stmt.execute("create table XML_TEST(COL varchar(128))");

        derbyInited = true;
    }

    public static void initFirebird() throws SQLException, ClassNotFoundException {
        targetHolder.set(Target.FIREBIRD);
        SQLTemplates templates = new FirebirdTemplates();
        Connection c = getFirebird();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        if (firebirdInited) {
            return;
        }

        try {
            stmt.execute("DECLARE EXTERNAL FUNCTION ltrim\n" +
                    "   CSTRING(255)\n" +
                    "   RETURNS CSTRING(255) FREE_IT\n" +
                    "   ENTRY_POINT 'IB_UDF_ltrim' MODULE_NAME 'ib_udf'");
        } catch (SQLException e) {
            // do nothing
        }
        try {
            stmt.execute("DECLARE EXTERNAL FUNCTION rtrim\n" +
                    "   CSTRING(255) NULL\n" +
                    "   RETURNS CSTRING(255) FREE_IT\n" +
                    "   ENTRY_POINT 'IB_UDF_rtrim' MODULE_NAME 'ib_udf'");
        } catch (SQLException e) {
            // do nothing
        }

        // survey
        dropTable(templates, "SURVEY");
        stmt.execute("create table SURVEY(ID int primary key, " +
                "NAME varchar(30)," +
                "NAME2 varchar(30))");

        stmt.execute("insert into SURVEY values (1,'Hello World','Hello');");

        try {
            //stmt.execute("DROP TRIGGER survey_auto_id;");
            stmt.execute("DROP GENERATOR survey_gen_id;");
        } catch (SQLException e) {
            // do nothing
        }

        stmt.execute("CREATE GENERATOR survey_gen_id;");
        stmt.execute("SET GENERATOR survey_gen_id TO 30;");
        stmt.execute("CREATE TRIGGER survey_auto_id FOR survey\n" +
                "ACTIVE BEFORE INSERT POSITION 0\n" +
                "AS\n" +
                "BEGIN\n" +
                "IF (NEW.id IS NULL) THEN\n" +
                "NEW.id = GEN_ID(survey_gen_id,1);\n" +
                "END ");

        // test
        dropTable(templates, "TEST");
        stmt.execute(CREATE_TABLE_TEST);
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
        try {
            for (int i = 0; i < TEST_ROW_COUNT; i++) {
                pstmt.setString(1, "name" + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            pstmt.close();
        }

        // employee
        dropTable(templates, "EMPLOYEE");
        //createEmployeeTable(templates);
        stmt.execute("create table EMPLOYEE ( " +
                "ID INT PRIMARY KEY, " +
                "FIRSTNAME VARCHAR(50), " +
                "LASTNAME VARCHAR(50), " +
                "SALARY DECIMAL, "  +
                "DATEFIELD DATE, " +
                "TIMEFIELD TIME, " +
                "SUPERIOR_ID INT, " +
                "CONSTRAINT FK_SUPERIOR FOREIGN KEY(SUPERIOR_ID) REFERENCES EMPLOYEE(ID) " +
                ")");

        try {
            //stmt.execute("DROP TRIGGER employee_auto_id;");
            stmt.execute("DROP GENERATOR employee_gen_id;");
        } catch (SQLException e) {
            // do nothing
        }

        stmt.execute("CREATE GENERATOR employee_gen_id;");
        stmt.execute("SET GENERATOR employee_gen_id TO 30;");
        stmt.execute("CREATE TRIGGER employee_auto_id FOR employee\n" +
                "ACTIVE BEFORE INSERT POSITION 0\n" +
                "AS\n" +
                "BEGIN\n" +
                "IF (NEW.id IS NULL) THEN\n" +
                "NEW.id = GEN_ID(employee_gen_id,1);\n" +
                "END ");

        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        dropTable(templates, "TIME_TEST");
        dropTable(templates, "DATE_TEST");
        stmt.execute(CREATE_TABLE_TIMETEST);
        stmt.execute(CREATE_TABLE_DATETEST);

        // numbers
        dropTable(templates, "NUMBER_TEST");
        stmt.execute("create table NUMBER_TEST(col1 char(1))");

        // xml
        dropTable(templates, "XML_TEST");
        stmt.execute("create table XML_TEST(COL varchar(128))");

        firebirdInited = true;
    }

    public static void initH2() throws SQLException, ClassNotFoundException {
        targetHolder.set(Target.H2);
        SQLTemplates templates = new H2Templates();
        Connection c = getH2();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        if (h2Inited) {
            return;
        }

        stmt.execute("DROP ALIAS IF EXISTS InitGeoDB");
        stmt.execute("CREATE ALIAS InitGeoDB for \"geodb.GeoDB.InitGeoDB\"");
        stmt.execute("CALL InitGeoDB()");

        // shapes
        dropTable(templates, "SHAPES");
        stmt.execute("create table SHAPES (ID int not null primary key, GEOMETRY blob)");
        for (Map.Entry<Integer, String> entry : getSpatialData().entrySet()) {
            stmt.execute("insert into SHAPES values(" + entry.getKey()
                    + ", ST_GeomFromText('" + entry.getValue() + "', 4326))");
        }

        // qtest
        stmt.execute("drop table QTEST if exists");
        stmt.execute("create table QTEST (ID int IDENTITY(1,1) NOT NULL,  C1 int NULL)");

        // uuids
        stmt.execute("drop table if exists UUIDS");
        stmt.execute("create table UUIDS (FIELD uuid)");

        // survey
        stmt.execute("drop table SURVEY if exists");
        stmt.execute(CREATE_TABLE_SURVEY);
        stmt.execute("insert into SURVEY values (1, 'Hello World', 'Hello');");
        stmt.execute("alter table SURVEY alter column id int auto_increment");

        // test
        stmt.execute("drop table TEST if exists");
        stmt.execute(CREATE_TABLE_TEST);
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
        try {
            for (int i = 0; i < TEST_ROW_COUNT; i++) {
                pstmt.setString(1, "name" + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            pstmt.close();
        }

        // employee
        stmt.execute("drop table EMPLOYEE if exists");
        createEmployeeTable(templates);
        stmt.execute("alter table EMPLOYEE alter column id int auto_increment");
        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        stmt.execute("drop table TIME_TEST if exists");
        stmt.execute("drop table DATE_TEST if exists");
        stmt.execute(CREATE_TABLE_TIMETEST);
        stmt.execute(CREATE_TABLE_DATETEST);

        // numbers
        dropTable(templates, "NUMBER_TEST");
        stmt.execute("create table NUMBER_TEST(col1 boolean)");

        // xml
        dropTable(templates, "XML_TEST");
        stmt.execute("create table XML_TEST(COL varchar(128))");

        h2Inited = true;
    }

    public static void initHSQL() throws SQLException, ClassNotFoundException {
        targetHolder.set(Target.HSQLDB);
        SQLTemplates templates = new HSQLDBTemplates();
        Connection c = getHSQL();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        if (hsqlInited) {
            return;
        }

        // arrays
        stmt.execute("drop table ARRAYTEST if exists");
        stmt.execute("create table ARRAYTEST ( " +
                "ID bigint primary key, " +
                "INTEGERS integer array, " +
                "MYARRAY varchar(8) array)");

        // dual
        stmt.execute("drop table DUAL if exists");
        stmt.execute("create table DUAL ( DUMMY varchar(1) )");
        stmt.execute("insert into DUAL (DUMMY) values ('X')");

        // survey
        stmt.execute("drop table SURVEY if exists");
        //stmt.execute(CREATE_TABLE_SURVEY);
        stmt.execute("create table SURVEY(" +
                "ID int generated by default as identity, " +
                "NAME varchar(30)," +
                "NAME2 varchar(30))");
        stmt.execute("insert into SURVEY values (1, 'Hello World', 'Hello')");

        // test
        stmt.execute("drop table TEST if exists");
        stmt.execute(CREATE_TABLE_TEST);
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
        try {
            for (int i = 0; i < TEST_ROW_COUNT; i++) {
                pstmt.setString(1, "name" + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            pstmt.close();
        }

        // employee
        stmt.execute("drop table EMPLOYEE if exists");
        createEmployeeTable(templates);
        stmt.execute("alter table EMPLOYEE alter column id int generated by default as identity");
        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        stmt.execute("drop table TIME_TEST if exists");
        stmt.execute("drop table DATE_TEST if exists");
        stmt.execute(CREATE_TABLE_TIMETEST);
        stmt.execute(CREATE_TABLE_DATETEST);

        // numbers
        dropTable(templates, "NUMBER_TEST");
        stmt.execute("create table NUMBER_TEST(col1 boolean)");

        // xml
        dropTable(templates, "XML_TEST");
        stmt.execute("create table XML_TEST(COL varchar(128))");

        hsqlInited = true;
    }

    public static void initMySQL() throws SQLException, ClassNotFoundException {
        targetHolder.set(Target.MYSQL);
        //SQLTemplates templates = new MySQLTemplates();
        Connection c = getMySQL();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        if (mysqlInited) {
            return;
        }

        // shapes
        stmt.execute("drop table if exists SHAPES");
        stmt.execute("create table SHAPES (ID int not null primary key, GEOMETRY geometry)");
        for (Map.Entry<Integer, String> entry : getSpatialData().entrySet()) {
            stmt.execute("insert into SHAPES values(" + entry.getKey()
                    + ", GeomFromText('" + entry.getValue() + "'))");
        }

        // survey
        stmt.execute("drop table if exists SURVEY");
        stmt.execute("create table SURVEY(ID int primary key auto_increment, " +
                "NAME varchar(30)," +
                "NAME2 varchar(30))");
        stmt.execute("insert into SURVEY values (1,'Hello World','Hello');");

        // test
        stmt.execute("drop table if exists TEST");
        stmt.execute(CREATE_TABLE_TEST);
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
        try {
            for (int i = 0; i < TEST_ROW_COUNT; i++) {
                pstmt.setString(1, "name" + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            pstmt.close();
        }

        // employee
        stmt.execute("drop table if exists EMPLOYEE");
        //createEmployeeTable(templates);
        stmt.execute("create table EMPLOYEE ( " +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "FIRSTNAME VARCHAR(50), " +
                "LASTNAME VARCHAR(50), " +
                "SALARY DECIMAL, "  +
                "DATEFIELD DATE, " +
                "TIMEFIELD TIME, " +
                "SUPERIOR_ID INT, " +
                "CONSTRAINT FK_SUPERIOR FOREIGN KEY(SUPERIOR_ID) REFERENCES EMPLOYEE(ID) " +
                ")");

        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        stmt.execute("drop table if exists TIME_TEST");
        stmt.execute("drop table if exists DATE_TEST");
        stmt.execute(CREATE_TABLE_TIMETEST);
        stmt.execute(CREATE_TABLE_DATETEST);

        // numbers
        stmt.execute("drop table if exists NUMBER_TEST");
        stmt.execute("create table NUMBER_TEST(col1 tinyint(1))");

        // xml
        stmt.execute("drop table if exists XML_TEST");
        stmt.execute("create table XML_TEST(COL varchar(128))");

        mysqlInited = true;
    }

    public static void initOracle() throws SQLException, ClassNotFoundException {
        targetHolder.set(Target.ORACLE);
        SQLTemplates templates = new OracleTemplates();
        Connection c = getOracle();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        if (oracleInited) {
            return;
        }

        // types
        stmt.execute("create or replace type ssn_t as object (ssn_type char(11))");

        // survey
        dropTable(templates, "SURVEY");
        stmt.execute("create table SURVEY (ID number(10,0), " +
                "NAME varchar(30 char)," +
                "NAME2 varchar(30 char))");

        try {
            stmt.execute("drop sequence survey_seq");
        } catch (SQLException e) {
            if (!e.getMessage().contains("sequence does not exist")) {
                throw e;
            }
        }

        stmt.execute("create sequence survey_seq");
        stmt.execute("create or replace trigger survey_trigger\n" +
                "before insert on survey\n" +
                "for each row\n"  +
                "when (new.id is null)\n" +
                "begin\n" +
                "  select survey_seq.nextval into :new.id from dual;\n" +
                "end;\n");

        stmt.execute("insert into SURVEY values (1,'Hello World','Hello')");

        // test
        dropTable(templates, "TEST");
        stmt.execute("create table TEST(name varchar(255))");
        String sql  = "insert into TEST values(?)";
        PreparedStatement pstmt = c.prepareStatement(sql);
        for (int i = 0; i < TEST_ROW_COUNT; i++) {
            pstmt.setString(1, "name" + i);
            pstmt.addBatch();
        }
        pstmt.executeBatch();

        // employee
        dropTable(templates, "EMPLOYEE");
        stmt.execute("create table EMPLOYEE ( " +
                "ID NUMBER(10,0), " +
                "FIRSTNAME VARCHAR2(50 CHAR), " +
                "LASTNAME VARCHAR2(50 CHAR), " +
                "SALARY DOUBLE PRECISION, " +
                "DATEFIELD DATE, " +
                "TIMEFIELD TIMESTAMP, " +
                "SUPERIOR_ID NUMBER(10,0), " +
                "CONSTRAINT PK_EMPLOYEE PRIMARY KEY(ID), " +
                "CONSTRAINT FK_SUPERIOR FOREIGN KEY(SUPERIOR_ID) REFERENCES EMPLOYEE(ID)" +
                ")");

        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        dropTable(templates, "DATE_TEST");
        stmt.execute("create table date_test(date_test date)");

        // numbers
        dropTable(templates, "NUMBER_TEST");
        stmt.execute("create table NUMBER_TEST(col1 number(1,0))");

        // xml
        dropTable(templates, "XML_TEST");
        stmt.execute("create table XML_TEST(COL XMLTYPE)");

        oracleInited = true;
    }

    public static void initPostgreSQL() throws SQLException, ClassNotFoundException {
        targetHolder.set(Target.POSTGRESQL);
        SQLTemplates templates = new PostgreSQLTemplates(true);
        // NOTE : unquoted identifiers are converted to lower case in PostgreSQL
        Connection c = getPostgreSQL();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        if (postgresqlInited) {
            return;
        }

        // shapes
        dropTable(templates, "SHAPES");
//        stmt.execute("create table \"SHAPES\" (\"ID\" int not null primary key, \"GEOMETRY\" geography(POINT,4326))");
        stmt.execute("create table \"SHAPES\" (\"ID\" int not null primary key)");
        stmt.execute("select AddGeometryColumn('SHAPES', 'GEOMETRY', -1, 'GEOMETRY', 2)");
        for (Map.Entry<Integer, String> entry : getSpatialData().entrySet()) {
            stmt.execute("insert into \"SHAPES\" values(" + entry.getKey()
                    + ", '" + entry.getValue() + "')");
        }

        // types
        dropType(stmt, "u_country");
        stmt.execute("create type u_country as enum ('Brazil', 'England', 'Germany')");

        dropType(stmt, "u_street_type");
        stmt.execute("create type u_street_type as (street VARCHAR(100), number VARCHAR(30))");

        // arrays
        dropTable(templates, "ARRAYTEST");
        stmt.execute("create table \"ARRAYTEST\" (\n" +
                "\"ID\" bigint primary key,\n" +
                "\"INTEGERS\" integer[],\n" +
                "\"MYARRAY\" varchar(8)[])");

        // uuids
        dropTable(templates, "UUIDS");
        stmt.execute("create table \"UUIDS\" (\"FIELD\" uuid)");

        // survey
        dropTable(templates, "SURVEY");
        try {
            stmt.execute("drop sequence SURVEY_SEQ");
        } catch (SQLException e) {
            if (!e.getMessage().contains("does not exist")) {
                throw e;
            }
        }
        stmt.execute("create sequence SURVEY_SEQ");
        stmt.execute("create table \"SURVEY\"(" +
                "\"ID\" int DEFAULT NEXTVAL('SURVEY_SEQ'), " +
                "\"NAME\" varchar(30), \"NAME2\" varchar(30))");
        stmt.execute("insert into \"SURVEY\" values (1, 'Hello World', 'Hello')");

        // test
        dropTable(templates, "TEST");
        stmt.execute(quote(CREATE_TABLE_TEST,"TEST","NAME"));
        String sql = quote(INSERT_INTO_TEST_VALUES,"TEST");
        PreparedStatement pstmt = c.prepareStatement(sql);
        try {
            for (int i = 0; i < TEST_ROW_COUNT; i++) {
                pstmt.setString(1, "name" + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            pstmt.close();
        }

        // employee
        // stmt.execute("drop table employee if exists");
        dropTable(templates, "EMPLOYEE");
        createEmployeeTable(templates);
        addEmployees("insert into \"EMPLOYEE\" " +
                "(\"ID\", \"FIRSTNAME\", \"LASTNAME\", \"SALARY\", \"DATEFIELD\", \"TIMEFIELD\", \"SUPERIOR_ID\") " +
                "values (?,?,?,?,?,?,?)");

        // date_test and time_test
        dropTable(templates, "TIME_TEST");
        dropTable(templates, "DATE_TEST");
        stmt.execute(quote(CREATE_TABLE_TIMETEST, "TIME_TEST"));
        stmt.execute(quote(CREATE_TABLE_DATETEST, "DATE_TEST"));

        // numbers
        dropTable(templates, "NUMBER_TEST");
        stmt.execute("create table \"NUMBER_TEST\"(\"COL1\" boolean)");

        // xml
        dropTable(templates, "XML_TEST");
        stmt.execute("create table \"XML_TEST\"(\"COL\" XML)");

        postgresqlInited = true;
    }

    public static void initSQLite() throws SQLException, ClassNotFoundException {
        targetHolder.set(Target.SQLITE);
//        SQLTemplates templates = new SQLiteTemplates();
        Connection c = getSQLite();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        if (sqliteInited) {
            return;
        }

        // qtest
        stmt.execute("drop table if exists QTEST");
        stmt.execute("create table QTEST (ID int IDENTITY(1,1) NOT NULL,  C1 int NULL)");

        // survey
        stmt.execute("drop table if exists SURVEY");
        stmt.execute("create table SURVEY(ID int auto_increment, " +
                "NAME varchar(30)," +
                "NAME2 varchar(30)," +
                "constraint survey_pk primary key(ID))");
        stmt.execute("insert into SURVEY values (1,'Hello World','Hello');");

        // test
        stmt.execute("drop table if exists TEST");
        stmt.execute(CREATE_TABLE_TEST);
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
        try {
            for (int i = 0; i < TEST_ROW_COUNT; i++) {
                pstmt.setString(1, "name" + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            pstmt.close();
        }

        // employee
        stmt.execute("drop table if exists EMPLOYEE");
        stmt.execute("create table EMPLOYEE ( " +
                "ID INT AUTO_INCREMENT, " +
                "FIRSTNAME VARCHAR(50), " +
                "LASTNAME VARCHAR(50), " +
                "SALARY DECIMAL, "  +
                "DATEFIELD DATE, " +
                "TIMEFIELD TIME, " +
                "SUPERIOR_ID INT, " +
                "CONSTRAINT PK_EMPLOYEE PRIMARY KEY(ID)," +
                "CONSTRAINT FK_SUPERIOR FOREIGN KEY(SUPERIOR_ID) REFERENCES EMPLOYEE(ID) " +
                ")");
        addEmployees(INSERT_INTO_EMPLOYEE);


        // date_test and time_test
        stmt.execute("drop table if exists TIME_TEST");
        stmt.execute("drop table if exists DATE_TEST");
        stmt.execute(CREATE_TABLE_TIMETEST);
        stmt.execute(CREATE_TABLE_DATETEST);

        // numbers
        stmt.execute("drop table if exists NUMBER_TEST");
        stmt.execute("create table NUMBER_TEST(col1 integer)");

        // xml
        stmt.execute("drop table if exists XML_TEST");
        stmt.execute("create table XML_TEST(COL varchar(128))");

        sqliteInited = true;
    }

    public static void initSQLServer() throws SQLException, ClassNotFoundException {
        targetHolder.set(Target.SQLSERVER);
        SQLTemplates templates = new SQLServerTemplates();
        Connection c = getSQLServer();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        if (sqlServerInited) {
            return;
        }

        dropTable(templates, "SHAPES");
        stmt.execute("create table SHAPES (ID int not null primary key, GEOMETRY geometry)");
        for (Map.Entry<Integer, String> entry : getSpatialData().entrySet()) {
            stmt.execute("insert into SHAPES values(" + entry.getKey()
                    + ", geometry::STGeomFromText('" + entry.getValue() + "', 0))");
        }

        // survey
        dropTable(templates, "SURVEY");
        stmt.execute("create table SURVEY(ID int, NAME varchar(30), NAME2 varchar(30))");
        stmt.execute("insert into SURVEY values (1, 'Hello World', 'Hello')");

        // test
        dropTable(templates, "TEST");
        stmt.execute(CREATE_TABLE_TEST);
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
        try {
            for (int i = 0; i < TEST_ROW_COUNT; i++) {
                pstmt.setString(1, "name" + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            pstmt.close();
        }

        // employee
        dropTable(templates, "EMPLOYEE");
        createEmployeeTable(templates);
        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        dropTable(templates, "TIME_TEST");
        dropTable(templates, "DATE_TEST");
        stmt.execute(CREATE_TABLE_TIMETEST);
        stmt.execute(CREATE_TABLE_DATETEST);

        // numbers
        dropTable(templates, "NUMBER_TEST");
        stmt.execute("create table NUMBER_TEST(col1 bit)");

        // xml
        dropTable(templates, "XML_TEST");
        stmt.execute("create table XML_TEST(COL XML)");

        sqlServerInited = true;
    }

    public static void initTeradata() throws SQLException, ClassNotFoundException {
        targetHolder.set(Target.TERADATA);
        SQLTemplates templates = new TeradataTemplates();
        Connection c = getTeradata();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        if (teradataInited) {
            return;
        }

        String identity = "GENERATED ALWAYS AS IDENTITY(START WITH 1 INCREMENT BY 1)";

        // shapes
        dropTable(templates, "SHAPES");
        stmt.execute("create table SHAPES (ID int not null primary key, GEOMETRY ST_GEOMETRY)");
        for (Map.Entry<Integer, String> entry : getSpatialData().entrySet()) {
            stmt.execute("insert into SHAPES values(" + entry.getKey()
                    + ", '" + entry.getValue() + "')");
        }

        // qtest
        dropTable(templates, "QTEST");
        stmt.execute("create table QTEST (ID int " + identity + " NOT NULL, C1 int NULL)");

        // survey
        dropTable(templates, "SURVEY");
        stmt.execute("create table SURVEY(ID int " + identity + ", NAME varchar(30), NAME2 varchar(30))");
        stmt.execute("insert into SURVEY values (1, 'Hello World', 'Hello');");

        // test
        dropTable(templates, "TEST");
        stmt.execute(CREATE_TABLE_TEST);
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
        try {
            for (int i = 0; i < TEST_ROW_COUNT; i++) {
                pstmt.setString(1, "name" + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } finally {
            pstmt.close();
        }

        // employee
        dropTable(templates, "EMPLOYEE");
        stmt.execute("create table EMPLOYEE (\n" +
                "ID INTEGER NOT NULL PRIMARY KEY, \n" +
                "FIRSTNAME VARCHAR(100),\n" +
                "LASTNAME VARCHAR(100),\n" +
                "SALARY DOUBLE PRECISION,\n" +
                "DATEFIELD DATE,\n" +
                "TIMEFIELD TIME,\n" +
                "SUPERIOR_ID INTEGER,\n" +
                "CONSTRAINT FK_SUPERIOR FOREIGN KEY(SUPERIOR_ID) REFERENCES EMPLOYEE(ID))");
        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        dropTable(templates, "TIME_TEST");
        dropTable(templates, "DATE_TEST");
        stmt.execute(CREATE_TABLE_TIMETEST);
        stmt.execute(CREATE_TABLE_DATETEST);

        // numbers
        dropTable(templates, "NUMBER_TEST");
        stmt.execute("create table NUMBER_TEST(ID int " + identity + " NOT NULL, col1 int)");

        // xml
        dropTable(templates, "XML_TEST");
        stmt.execute("create table XML_TEST(ID int " + identity + " NOT NULL, COL varchar(128))");

        teradataInited = true;
    }

    static void addEmployee(String sql, int id, String firstName, String lastName,
                            double salary, int superiorId) throws SQLException {
        PreparedStatement stmt = connHolder.get().prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, firstName);
        stmt.setString(3,lastName);
        stmt.setDouble(4, salary);
        stmt.setDate(5, Constants.date);
        stmt.setTime(6, Constants.time);
        if (superiorId <= 0) {
            stmt.setNull(7, Types.INTEGER);
        } else {
            stmt.setInt(7, superiorId);
        }
        stmt.execute();
        stmt.close();
    }

    private static void addEmployees(String sql) throws SQLException {
        addEmployee(sql, 1, "Mike", "Smith", 160000, -1);
        addEmployee(sql, 2, "Mary", "Smith", 140000, -1);

        // Employee under Mike
        addEmployee(sql, 10, "Joe", "Divis", 50000, 1);
        addEmployee(sql, 11, "Peter", "Mason", 45000, 1);
        addEmployee(sql, 12, "Steve", "Johnson", 40000, 1);
        addEmployee(sql, 13, "Jim", "Hood", 35000, 1);

        // Employee under Mike
        addEmployee(sql, 20, "Jennifer", "Divis", 60000, 2);
        addEmployee(sql, 21, "Helen", "Mason", 50000, 2);
        addEmployee(sql, 22, "Daisy", "Johnson", 40000, 2);
        addEmployee(sql, 23, "Barbara", "Hood", 30000, 2);
    }

    private static String quote(String sql, String... identifiers) {
        String rv = sql;
        for (String id : identifiers) {
            rv = rv.replace(id,  "\"" + id +  "\"");
        }
        return rv;
    }

    private Connections() { }
}
