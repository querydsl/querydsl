package com.querydsl.sql.codegen;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.querydsl.sql.Connections;

public class ExportH2TwoSchemasTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initH2();

        Statement stmt = Connections.getStatement();
        stmt.execute("create schema if not exists newschema");
        stmt.execute("create table if not exists " +
                "newschema.SURVEY(ID2 int auto_increment, NAME2 varchar(30), NAME3 varchar(30))");
    }

    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
        Connections.close();
    }

    @Test
    public void Export() throws SQLException, MalformedURLException, IOException {
        File folder = new File("target", getClass().getSimpleName());
        folder.mkdirs();
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setSchemaPattern(null);
        exporter.setPackageName("test");
        exporter.setTargetFolder(folder);
        exporter.setNamingStrategy(namingStrategy);
        exporter.export(Connections.getConnection().getMetaData());

        String contents = Resources.toString(new File(folder, "test/QSurvey.java").toURI().toURL(),
                Charsets.UTF_8);
        assertTrue(contents.contains("id"));
        assertTrue(contents.contains("name"));
        assertTrue(contents.contains("name2"));

        assertFalse(contents.contains("id2"));
        assertFalse(contents.contains("name3"));
    }

}
