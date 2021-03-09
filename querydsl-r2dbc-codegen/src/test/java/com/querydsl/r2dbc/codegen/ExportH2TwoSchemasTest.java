package com.querydsl.r2dbc.codegen;

import com.querydsl.core.testutil.H2;
import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.SQLTemplates;
import com.querydsl.sql.Connections;
import com.querydsl.sql.codegen.DefaultNamingStrategy;
import com.querydsl.sql.codegen.NamingStrategy;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Category(H2.class)
public class ExportH2TwoSchemasTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initH2();

        Statement stmt = Connections.getStatement();
        stmt.execute("create schema if not exists newschema");
        stmt.execute("create table if not exists " +
                "newschema.SURVEY2(ID2 int auto_increment, NAME2 varchar(30), NAME3 varchar(30))");
    }

    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
        Connections.close();
    }

    @Test
    public void export() throws SQLException, MalformedURLException, IOException {
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setSchemaPattern(null);
        exporter.setPackageName("test");
        exporter.setTargetFolder(folder.getRoot());
        exporter.setNamingStrategy(namingStrategy);
        exporter.setConfiguration(new Configuration(SQLTemplates.DEFAULT));
        exporter.export(Connections.getConnection().getMetaData());

        String contents = new String(Files.readAllBytes(new File(folder.getRoot(), "test/QSurvey.java").toPath()), StandardCharsets.UTF_8);
        assertTrue(contents.contains("id"));
        assertTrue(contents.contains("name"));
        assertTrue(contents.contains("name2"));

        assertFalse(contents.contains("id2"));
        assertFalse(contents.contains("name3"));
    }

}
