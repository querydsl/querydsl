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
package com.querydsl.r2dbc;

import com.querydsl.core.Target;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.r2dbc.binding.BindMarkers;
import com.querydsl.r2dbc.binding.BindTarget;
import com.querydsl.r2dbc.binding.StatementWrapper;
import com.querydsl.r2dbc.ddl.CreateTableClause;
import com.querydsl.r2dbc.ddl.DropTableClause;
import com.querydsl.r2dbc.domain.QEmployee;
import io.r2dbc.spi.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mc_fish
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

    private static boolean sqlServerInited, h2Inited, mysqlInited, postgresqlInited;

    public static R2DBCConnectionProvider getR2DBCConnectionProvider(String url) {
        return () -> Mono.from(getConnectionProvider(url).create());
    }

    public static ConnectionFactory getConnectionProvider(String url) {
        return ConnectionFactories.get(url);
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

    public static R2DBCConnectionProvider getH2() {
        String url = "r2dbc:h2:file://././target/h2-test;LOCK_MODE=0";
        return getR2DBCConnectionProvider(url);
    }

    public static R2DBCConnectionProvider getMySQL() {
        String url = "r2dbc:mysql://querydsl:querydsl@localhost:3306/querydsl?useLegacyDatetimeCode=false";
        return getR2DBCConnectionProvider(url);
    }

    public static R2DBCConnectionProvider getPostgreSQL() {
        String url = "r2dbc:postgresql://querydsl:querydsl@localhost:5433/querydsl";
        return getR2DBCConnectionProvider(url);
    }

    public static R2DBCConnectionProvider getSQLServer() {
        String url = "r2dbc:mssql://sa:Password1!@localhost:1433/tempdb";
        return getR2DBCConnectionProvider(url);
    }

    public static CreateTableClause createTable(SQLTemplates templates, String table) {
        return new CreateTableClause(connHolder.get(), new Configuration(templates), table);
    }

    public static Mono<Void> dropTable(SQLTemplates templates, String table) {
        return new DropTableClause(connHolder.get(), new Configuration(templates), table).execute();
    }

    public static Mono<Void> dropType(Connection connection, String type) {
        return execute(connection, "drop type " + type).then();
    }

    public static Mono<Result> execute(Connection connection, String query) {
        Statement statement = connection.createStatement(query);
        return Mono.from(statement.execute());
    }

    private static Mono<Void> createEmployeeTable(SQLTemplates templates) {
        return createTable(templates, "EMPLOYEE")
                .column("ID", Integer.class).notNull()
                .column("FIRSTNAME", String.class).size(50)
                .column("LASTNAME", String.class).size(50)
                .column("SALARY", BigDecimal.class)
                .column("DATEFIELD", LocalDate.class)
                .column("TIMEFIELD", LocalTime.class)
                .column("SUPERIOR_ID", Integer.class)
                .primaryKey("PK_EMPLOYEE", "ID")
                .foreignKey("FK_SUPERIOR", "SUPERIOR_ID").references("EMPLOYEE", "ID")
                .execute();
    }

    public static Map<Integer, String> getSpatialData() {
        Map<Integer, String> m = new HashMap<>();
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

    public static void initH2() {
        targetHolder.set(Target.H2);
        SQLTemplates templates = new H2Templates();
        R2DBCConnectionProvider c = getH2();
        Connection connection = c.getConnection().block();
        connHolder.set(connection);

        if (h2Inited) {
            return;
        }

        Flux<Void> setup = Flux.concat(
//                execute(connection, "DROP ALIAS IF EXISTS InitGeoDB").then(),
//                execute(connection, "CREATE ALIAS InitGeoDB for geodb.GeoDB.InitGeoDB").then(),
//                execute(connection, "CALL InitGeoDB()").then(),
                dropTable(templates, "SHAPES").then(),
                execute(connection, "create table SHAPES (ID int not null primary key, GEOMETRY blob)").then(),
                execute(connection, "drop table QTEST if exists").then(),
                execute(connection, "create table QTEST (ID int IDENTITY(1,1) NOT NULL,  C1 int NULL)").then(),
                execute(connection, "drop table if exists UUIDS").then(),
                execute(connection, "create table UUIDS (FIELD uuid)").then(),
                execute(connection, "drop table SURVEY if exists").then(),
                execute(connection, CREATE_TABLE_SURVEY).then(),
                execute(connection, "insert into SURVEY values (1, 'Hello World', 'Hello');").then(),
//                execute(connection, "alter table SURVEY alter column id int auto_increment").then(),
                execute(connection, "drop table TEST if exists").then(),
                execute(connection, CREATE_TABLE_TEST).then(),
                dropTable(templates, "EMPLOYEE").then(),
                createEmployeeTable(templates).then(),
                addEmployees(connection, INSERT_INTO_EMPLOYEE).then(),
                execute(connection, "alter table EMPLOYEE alter column id int auto_increment").then(),
                execute(connection, "drop table TIME_TEST if exists").then(),
                execute(connection, "drop table DATE_TEST if exists").then(),
                execute(connection, CREATE_TABLE_TIMETEST).then(),
                execute(connection, CREATE_TABLE_DATETEST).then(),
                dropTable(templates, "NUMBER_TEST").then(),
                execute(connection, "create table NUMBER_TEST(col1 boolean)").then(),
                dropTable(templates, "XML_TEST").then(),
                execute(connection, "create table XML_TEST(COL varchar(128))").then()
        );

//        for (Map.Entry<Integer, String> entry : getSpatialData().entrySet()) {
//            setup = setup.concatWith(execute(connection, "insert into SHAPES values(" + entry.getKey()
//                    + ", ST_GeomFromText('" + entry.getValue() + "', 4326))").then());
//        }

        List<Object> constants = Arrays.asList(0);
        String sql = R2dbcUtils.replaceBindingArguments(getConfiguration().getBindMarkerFactory().create(), constants, quote(INSERT_INTO_TEST_VALUES, "TEST"));
        Statement pstmt = connection.createStatement(sql);

        BindTarget bindTarget = new StatementWrapper(pstmt);

        for (int i = 0; i < TEST_ROW_COUNT; i++) {
            BindMarkers bindMarkers = getConfiguration().getBindMarkerFactory().create();
            getConfiguration().set(bindMarkers.next(), bindTarget, Expressions.stringPath("name"), "name" + i);
            pstmt.add();
        }

        setup = setup.concatWith(Mono.from(pstmt.execute()).then());

        StepVerifier
                .create(setup)
                .verifyComplete();

        h2Inited = true;
    }

    public static void initMySQL() {
        targetHolder.set(Target.MYSQL);
//        SQLTemplates templates = new MySQLTemplates();
        R2DBCConnectionProvider c = getMySQL();
        Connection connection = c.getConnection().block();
        connHolder.set(connection);

        if (mysqlInited) {
            return;
        }

        Flux<Void> setup = Flux.concat(
                execute(connection, "drop table if exists SHAPES").then(),
                execute(connection, "create table SHAPES (ID int not null primary key, GEOMETRY geometry)").then(),
                execute(connection, "drop table if exists SURVEY").then(),
                execute(connection, "create table SURVEY(ID int primary key auto_increment, NAME varchar(30), NAME2 varchar(30))").then(),
                execute(connection, "insert into SURVEY values (1,'Hello World','Hello');").then(),
                execute(connection, "drop table if exists TEST").then(),
                execute(connection, CREATE_TABLE_TEST).then(),
                execute(connection, "drop table if exists EMPLOYEE").then(),
                execute(connection, "create table EMPLOYEE ( " +
                        "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                        "FIRSTNAME VARCHAR(50), " +
                        "LASTNAME VARCHAR(50), " +
                        "SALARY DECIMAL, " +
                        "DATEFIELD DATE, " +
                        "TIMEFIELD TIME, " +
                        "SUPERIOR_ID INT, " +
                        "CONSTRAINT FK_SUPERIOR FOREIGN KEY(SUPERIOR_ID) REFERENCES EMPLOYEE(ID) )").then(),
                addEmployees(connection, INSERT_INTO_EMPLOYEE).then(),
                execute(connection, "drop table if exists TIME_TEST").then(),
                execute(connection, CREATE_TABLE_TIMETEST).then(),
                execute(connection, "drop table if exists DATE_TEST").then(),
                execute(connection, CREATE_TABLE_DATETEST).then(),
                execute(connection, "drop table if exists NUMBER_TEST").then(),
                execute(connection, "create table NUMBER_TEST(col1 tinyint(1))").then(),
                execute(connection, "drop table if exists XML_TEST").then(),
                execute(connection, "create table XML_TEST(COL varchar(128))").then()
        );

        for (Map.Entry<Integer, String> entry : getSpatialData().entrySet()) {
            setup = setup.concatWith(execute(connection, "insert into SHAPES values(" + entry.getKey()
                    + ", GeomFromText('" + entry.getValue() + "'))").then());
        }

        List<Object> constants = Arrays.asList(0);
        String sql = R2dbcUtils.replaceBindingArguments(getConfiguration().getBindMarkerFactory().create(), constants, INSERT_INTO_TEST_VALUES);
        Statement pstmt = connection.createStatement(sql);

        BindTarget bindTarget = new StatementWrapper(pstmt);

        for (int i = 0; i < TEST_ROW_COUNT; i++) {
            BindMarkers bindMarkers = getConfiguration().getBindMarkerFactory().create();
            getConfiguration().set(bindMarkers.next(), bindTarget, Expressions.stringPath("name"), "name" + i);
            pstmt.add();
        }

        setup = setup.concatWith(Mono.from(pstmt.execute()).then());

        StepVerifier
                .create(setup)
                .verifyComplete();

        mysqlInited = true;
    }

    public static void initPostgreSQL() {
        targetHolder.set(Target.POSTGRESQL);
        SQLTemplates templates = new PostgreSQLTemplates(true);
        // NOTE : unquoted identifiers are converted to lower case in PostgreSQL
        R2DBCConnectionProvider c = getPostgreSQL();
        Connection connection = c.getConnection().block();
        connHolder.set(connection);

        if (postgresqlInited) {
            return;
        }

        Flux<Void> setup = Flux.concat(
                dropTable(templates, "SHAPES").then(),
                execute(connection, "create table \"SHAPES\" (\"ID\" int not null primary key)").then(),
                execute(connection, "select AddGeometryColumn('SHAPES', 'GEOMETRY', -1, 'GEOMETRY', 2)").then(),
                dropType(connection, "u_country").then(),
                execute(connection, "create type u_country as enum ('Brazil', 'England', 'Germany')").then(),
                dropType(connection, "u_street_type").then(),
                execute(connection, "create type u_street_type as (street VARCHAR(100), number VARCHAR(30))").then(),
                dropTable(templates, "UUIDS").then(),
                execute(connection, "create table \"UUIDS\" (\"FIELD\" uuid)").then(),
                dropTable(templates, "SURVEY").then(),
                execute(connection, "drop sequence SURVEY_SEQ").then(),
                execute(connection, "create sequence SURVEY_SEQ").then(),
                execute(connection, "create table \"SURVEY\"(" +
                        "\"ID\" int DEFAULT NEXTVAL('SURVEY_SEQ'), " +
                        "\"NAME\" varchar(30), \"NAME2\" varchar(30))").then(),
                execute(connection, "insert into \"SURVEY\" values (1, 'Hello World', 'Hello')").then(),
                dropTable(templates, "TEST").then(),
                execute(connection, quote(CREATE_TABLE_TEST, "TEST", "NAME")).then(),
                dropTable(templates, "EMPLOYEE").then(),
                createEmployeeTable(templates).then(),
                addEmployees(connection, "insert into \"EMPLOYEE\" " +
                        "(\"ID\", \"FIRSTNAME\", \"LASTNAME\", \"SALARY\", \"DATEFIELD\", \"TIMEFIELD\", \"SUPERIOR_ID\") " +
                        "values (?,?,?,?,?,?,?)").then(),
                dropTable(templates, "TIME_TEST").then(),
                execute(connection, quote(CREATE_TABLE_TIMETEST, "TIME_TEST")).then(),
                dropTable(templates, "DATE_TEST").then(),
                execute(connection, quote(CREATE_TABLE_DATETEST, "DATE_TEST")).then(),
                dropTable(templates, "NUMBER_TEST").then(),
                execute(connection, "create table \"NUMBER_TEST\"(\"COL1\" boolean)").then()
        );

        for (Map.Entry<Integer, String> entry : getSpatialData().entrySet()) {
            setup = setup.concatWith(execute(connection, "insert into \"SHAPES\" values(" + entry.getKey()
                    + ", '" + entry.getValue() + "')").then());
        }

        List<Object> constants = Arrays.asList(0);
        String sql = R2dbcUtils.replaceBindingArguments(getConfiguration().getBindMarkerFactory().create(), constants, quote(INSERT_INTO_TEST_VALUES, "TEST"));
        Statement pstmt = connection.createStatement(sql);

        BindTarget bindTarget = new StatementWrapper(pstmt);

        for (int i = 0; i < TEST_ROW_COUNT; i++) {
            BindMarkers bindMarkers = getConfiguration().getBindMarkerFactory().create();
            getConfiguration().set(bindMarkers.next(), bindTarget, Expressions.stringPath("name"), "name" + i);
            pstmt.add();
        }

        setup = setup.concatWith(Mono.from(pstmt.execute()).then());

        StepVerifier
                .create(setup)
                .verifyComplete();

        postgresqlInited = true;
    }

    public static void initSQLServer() {
        targetHolder.set(Target.SQLSERVER);
        SQLTemplates templates = new SQLServerTemplates();
        R2DBCConnectionProvider c = getSQLServer();
        Connection connection = c.getConnection().block();
        connHolder.set(connection);

        if (sqlServerInited) {
            return;
        }

        Flux<Void> setup = Flux.concat(
                dropTable(templates, "SHAPES").then(),
                execute(connection, "create table SHAPES (ID int not null primary key, GEOMETRY geometry)").then(),
                dropTable(templates, "SURVEY").then(),
                execute(connection, "create table SURVEY(ID int, NAME varchar(30), NAME2 varchar(30))").then(),
                execute(connection, "insert into SURVEY values (1, 'Hello World', 'Hello')").then(),
                dropTable(templates, "TEST").then(),
                execute(connection, CREATE_TABLE_TEST).then(),
                dropTable(templates, "EMPLOYEE").then(),
                createEmployeeTable(templates).then(),
                addEmployees(connection, INSERT_INTO_EMPLOYEE).then(),
                dropTable(templates, "TIME_TEST").then(),
                execute(connection, CREATE_TABLE_TIMETEST).then(),
                dropTable(templates, "DATE_TEST").then(),
                execute(connection, CREATE_TABLE_DATETEST).then(),
                dropTable(templates, "NUMBER_TEST").then(),
                execute(connection, "create table NUMBER_TEST(COL1 tinyint)").then()
        );

        for (Map.Entry<Integer, String> entry : getSpatialData().entrySet()) {
            setup = setup.concatWith(execute(connection, "insert into SHAPES values(" + entry.getKey()
                    + ", geometry::STGeomFromText('" + entry.getValue() + "', 0))").then());
        }

        List<Object> constants = Arrays.asList(0);
        String sql = R2dbcUtils.replaceBindingArguments(getConfiguration().getBindMarkerFactory().create(), constants, quote(INSERT_INTO_TEST_VALUES, "TEST"));
        Statement pstmt = connection.createStatement(sql);

        BindTarget bindTarget = new StatementWrapper(pstmt);

        for (int i = 0; i < TEST_ROW_COUNT; i++) {
            BindMarkers bindMarkers = getConfiguration().getBindMarkerFactory().create();
            getConfiguration().set(bindMarkers.next(), bindTarget, Expressions.stringPath("name"), "name" + i);
            pstmt.add();
        }

        setup = setup.concatWith(Mono.from(pstmt.execute()).then());

        StepVerifier
                .create(setup)
                .verifyComplete();

        sqlServerInited = true;
    }

    static Mono<Void> addEmployee(Connection connection, String originalSql, int id, String firstName, String lastName,
                                  double salary, int superiorId) {
        Configuration configuration = configurationHolder.get();

        List<Object> constants = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
        String sql = R2dbcUtils.replaceBindingArguments(configuration.getBindMarkerFactory().create(), constants, originalSql);

        Statement statement = connection.createStatement(sql);
        BindTarget bindTarget = new StatementWrapper(statement);
        BindMarkers bindMarkers = configuration.getBindMarkerFactory().create();

        configuration.set(bindMarkers.next(), bindTarget, QEmployee.employee.id, id);
        configuration.set(bindMarkers.next(), bindTarget, QEmployee.employee.firstname, firstName);
        configuration.set(bindMarkers.next(), bindTarget, QEmployee.employee.lastname, lastName);
        configuration.set(bindMarkers.next(), bindTarget, QEmployee.employee.salary, salary);
        configuration.set(bindMarkers.next(), bindTarget, QEmployee.employee.datefield, Constants.localDate);
        configuration.set(bindMarkers.next(), bindTarget, QEmployee.employee.timefield, Constants.localTime);
        if (superiorId <= 0) {
            configuration.set(bindMarkers.next(), bindTarget, QEmployee.employee.superiorId, null);
        } else {
            configuration.set(bindMarkers.next(), bindTarget, QEmployee.employee.superiorId, superiorId);
        }

        return Mono.from(statement.execute()).then();
    }

    private static Flux<Void> addEmployees(Connection connection, String sql) {
        return Flux.concat(
                addEmployee(connection, sql, 1, "Mike", "Smith", 160000, -1),
                addEmployee(connection, sql, 2, "Mary", "Smith", 140000, -1),

                // Employee under Mike
                addEmployee(connection, sql, 10, "Joe", "Divis", 50000, 1),
                addEmployee(connection, sql, 11, "Peter", "Mason", 45000, 1),
                addEmployee(connection, sql, 12, "Steve", "Johnson", 40000, 1),
                addEmployee(connection, sql, 13, "Jim", "Hood", 35000, 1),

                // Employee under Mike
                addEmployee(connection, sql, 20, "Jennifer", "Divis", 60000, 2),
                addEmployee(connection, sql, 21, "Helen", "Mason", 50000, 2),
                addEmployee(connection, sql, 22, "Daisy", "Johnson", 40000, 2),
                addEmployee(connection, sql, 23, "Barbara", "Hood", 30000, 2)
        );
    }

    private static String quote(String sql, String... identifiers) {
        String rv = sql;
        for (String id : identifiers) {
            rv = rv.replace(id, "" + id + "");
        }
        return rv;
    }

    private Connections() {
    }
}
