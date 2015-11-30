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
package com.querydsl.sql.codegen;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;

import org.junit.*;

import com.mysema.codegen.SimpleCompiler;
import com.querydsl.codegen.BeanSerializer;
import com.querydsl.codegen.Serializer;
import com.querydsl.core.util.FileUtils;

public class MetaDataExporterTest {

    private static final List<Serializer> BEAN_SERIALIZERS = Arrays.<Serializer>asList(
            new BeanSerializer());

    private static Connection connection;

    private Serializer beanSerializer;

    private boolean clean = true;

    private boolean exportColumns = false;

    private boolean schemaToPackage = false;

    private DatabaseMetaData metadata;

    private JavaCompiler compiler = new SimpleCompiler();

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:mem:testdb" + System.currentTimeMillis();
        connection = DriverManager.getConnection(url, "sa", "");
        createTables(connection);
    }

    static void createTables(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();

        try {
            // reserved words
            stmt.execute("create table reserved (id int, while int)");

            stmt.execute("create table class (id int)");

            // underscore
            stmt.execute("create table underscore (e_id int, c_id int)");

            // bean generation
            stmt.execute("create table beangen1 (\"SEP_Order\" int)");

            // default instance clash
            stmt.execute("create table definstance (id int, definstance int, definstance1 int)");

            // class with pk and fk classes
            stmt.execute("create table pkfk (id int primary key, pk int, fk int)");

            // camel case
            stmt.execute("create table \"camelCase\" (id int)");
            stmt.execute("create table \"vwServiceName\" (id int)");

            // simple types
            stmt.execute("create table date_test (d date)");
            stmt.execute("create table date_time_test (dt datetime)");

            // complex type
            stmt.execute("create table survey (id int, name varchar(30))");

            // new line
            stmt.execute("create table \"new\nline\" (id int)");

            stmt.execute("create table newline2 (id int, \"new\nline\" int)");

            stmt.execute("create table employee("
                    + "id INT, "
                    + "firstname VARCHAR(50), "
                    + "lastname VARCHAR(50), "
                    + "salary DECIMAL(10, 2), "
                    + "datefield DATE, "
                    + "timefield TIME, "
                    + "superior_id int, "
                    + "survey_id int, "
                    + "survey_name varchar(30), "
                    + "CONSTRAINT PK_employee PRIMARY KEY (id), "
                    + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) REFERENCES employee(id))");

            // multi key
            stmt.execute("create table multikey(id INT, id2 VARCHAR, id3 INT, CONSTRAINT pk_multikey PRIMARY KEY (id, id2, id3) )");

            //  M_PRODUCT_BOM_ID
            stmt.execute("create table product(id int, "
                    + "m_product_bom_id int, "
                    + "m_productbom_id int, "
                    + "constraint product_bom foreign key (m_productbom_id) references product(id))");

        } finally {
            stmt.close();
        }
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        connection.close();
    }

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        metadata = connection.getMetaData();
    }


    private static final NamingStrategy defaultNaming = new DefaultNamingStrategy();

    private static final NamingStrategy originalNaming = new OriginalNamingStrategy();

    private String beanPackageName = null;

    @Test
    public void normalSettings_repetition() throws SQLException {
        test("Q", "", "", "", defaultNaming, "target/1", false, false, false);

        File file = new File("target/1/test/QEmployee.java");
        long lastModified = file.lastModified();
        assertTrue(file.exists());

        clean = false;
        test("Q", "", "", "", defaultNaming, "target/1", false, false, false);
        assertEquals(lastModified, file.lastModified());
    }

    @Test
    public void explicit_configuration() throws SQLException {
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setSchemaPattern("PUBLIC");
        exporter.setNamePrefix("Q");
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File("target/7"));
        exporter.setNamingStrategy(new DefaultNamingStrategy());
        exporter.setBeanSerializer(new BeanSerializer());
        exporter.setBeanPackageName("test2");
        exporter.export(metadata);

        assertTrue(new File("target/7/test/QDateTest.java").exists());
        assertTrue(new File("target/7/test2/DateTest.java").exists());
    }

    @Test
    public void minimal_configuration() throws SQLException {
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setSchemaPattern("PUBLIC");
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File("target/8"));
        exporter.export(metadata);

        assertTrue(new File("target/8/test/QDateTest.java").exists());
    }

    @Test
    public void minimal_configuration_with_tables() throws SQLException {
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setSchemaPattern("PUBLIC");
        exporter.setTableNamePattern("RESERVED,UNDERSCORE,BEANGEN1");
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File("target/82"));
        exporter.export(metadata);

        assertTrue(new File("target/82/test/QBeangen1.java").exists());
        assertTrue(new File("target/82/test/QReserved.java").exists());
        assertTrue(new File("target/82/test/QUnderscore.java").exists());
        assertFalse(new File("target/82/test/QDefinstance.java").exists());
    }

    @Test
    public void minimal_configuration_with_suffix() throws SQLException {
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setSchemaPattern("PUBLIC");
        exporter.setPackageName("test");
        exporter.setNamePrefix("");
        exporter.setNameSuffix("Type");
        exporter.setTargetFolder(new File("target/9"));
        exporter.export(metadata);

        assertTrue(new File("target/9/test/DateTestType.java").exists());
    }

    @Test
    public void minimal_configuration_without_keys() throws SQLException {
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setSchemaPattern("PUBLIC");
        exporter.setPackageName("test");
        exporter.setNamePrefix("");
        exporter.setNameSuffix("Type");
        exporter.setTargetFolder(new File("target/10"));
        exporter.setExportForeignKeys(false);
        exporter.export(metadata);

        assertTrue(new File("target/10/test/DateTestType.java").exists());
    }

    @Test
    public void minimal_configuration_with_bean_prefix() throws SQLException {
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setSchemaPattern("PUBLIC");
        exporter.setPackageName("test");
        exporter.setNamePrefix("");
        exporter.setBeanPrefix("Bean");
        exporter.setBeanSerializer(new BeanSerializer());
        exporter.setTargetFolder(new File("target/a"));
        exporter.export(metadata);

        assertTrue(new File("target/a/test/DateTest.java").exists());
        assertTrue(new File("target/a/test/BeanDateTest.java").exists());
    }

    @Test
    public void minimal_configuration_with_bean_suffix() throws SQLException {
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setSchemaPattern("PUBLIC");
        exporter.setPackageName("test");
        exporter.setNamePrefix("");
        exporter.setBeanSuffix("Bean");
        exporter.setBeanSerializer(new BeanSerializer());
        exporter.setTargetFolder(new File("target/b"));
        exporter.export(metadata);

        assertTrue(new File("target/b/test/DateTest.java").exists());
        assertTrue(new File("target/b/test/DateTestBean.java").exists());
    }

    @Test
    public void minimal_configuration_with_bean_folder() throws SQLException {
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setSchemaPattern("PUBLIC");
        exporter.setPackageName("test");
        exporter.setNamePrefix("");
        exporter.setBeanSuffix("Bean");
        exporter.setBeanSerializer(new BeanSerializer());
        exporter.setTargetFolder(new File("target/b1"));
        exporter.setBeansTargetFolder(new File("target/b2"));
        exporter.export(metadata);

        assertTrue(new File("target/b1/test/DateTest.java").exists());
        assertTrue(new File("target/b2/test/DateTestBean.java").exists());
    }

    private void test(String namePrefix, String nameSuffix, String beanPrefix, String beanSuffix,
            NamingStrategy namingStrategy, String target, boolean withBeans,
            boolean withInnerClasses, boolean withOrdinalPositioning) throws SQLException {
        File targetDir = new File(target);
        if (clean) {
            try {
                if (targetDir.exists()) {
                    FileUtils.delete(targetDir);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setColumnAnnotations(exportColumns);
        exporter.setSchemaPattern("PUBLIC");
        exporter.setNamePrefix(namePrefix);
        exporter.setNameSuffix(nameSuffix);
        exporter.setBeanPrefix(beanPrefix);
        exporter.setBeanSuffix(beanSuffix);
        exporter.setInnerClassesForKeys(withInnerClasses);
        exporter.setPackageName("test");
        exporter.setBeanPackageName(beanPackageName);
        exporter.setTargetFolder(targetDir);
        exporter.setNamingStrategy(namingStrategy);
        exporter.setSchemaToPackage(schemaToPackage);
        if (withBeans) {
            exporter.setBeanSerializer(beanSerializer);
        }
        if (withOrdinalPositioning) {
            exporter.setColumnComparatorClass(OrdinalPositionComparator.class);
        }
        exporter.export(metadata);

        Set<String> classes = exporter.getClasses();
        int compilationResult = compiler.run(null, System.out, System.err,
                classes.toArray(new String[classes.size()]));
        if (compilationResult != 0) {
            Assert.fail("Compilation Failed for " + target);
        }
    }

}
