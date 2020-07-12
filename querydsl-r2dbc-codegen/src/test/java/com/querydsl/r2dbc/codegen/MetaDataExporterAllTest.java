package com.querydsl.r2dbc.codegen;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;

import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.SQLTemplates;
import com.querydsl.sql.codegen.DefaultNamingStrategy;
import com.querydsl.sql.codegen.NamingStrategy;
import com.querydsl.sql.codegen.OrdinalPositionComparator;
import com.querydsl.sql.codegen.OriginalNamingStrategy;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.mysema.codegen.SimpleCompiler;
import com.querydsl.codegen.BeanSerializer;
import com.querydsl.core.testutil.Parallelized;
import com.querydsl.core.testutil.SlowTest;

@RunWith(value = Parallelized.class)
@Category(SlowTest.class)
public class MetaDataExporterAllTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private static Connection connection;
    private static DatabaseMetaData metadata;
    private static JavaCompiler compiler = new SimpleCompiler();

    private String namePrefix, nameSuffix, beanPrefix, beanSuffix;
    private String beanPackageName;
    private BeanSerializer beanSerializer = new BeanSerializer();
    private NamingStrategy namingStrategy;
    private boolean withBeans, withInnerClasses, withOrdinalPositioning;
    private boolean exportColumns, schemaToPackage;

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:mem:testdb" + System.currentTimeMillis();
        connection = DriverManager.getConnection(url, "sa", "");
        metadata = connection.getMetaData();
        MetaDataExporterTest.createTables(connection);
    }

    @AfterClass
    public static void tearDownClass() throws SQLException {
        connection.close();
    }

    @Parameterized.Parameters
    public static List<Object[]> parameters() {
        List<Object[]> params = Lists.newArrayList();

        List<NamingStrategy> ns = Arrays.<NamingStrategy>asList(
                new DefaultNamingStrategy(), new OriginalNamingStrategy());
        List<String> prefixOrSuffix = Arrays.asList("", "Q");
        List<String> beanPackage = Arrays.asList(null, "test2");
        List<Boolean> booleans = Arrays.asList(true, false);

        for (String namePrefix : prefixOrSuffix) {
            for (String nameSuffix : prefixOrSuffix) {
                for (String beanPrefix : prefixOrSuffix) {
                    for (String beanSuffix : prefixOrSuffix) {
                        for (String beanPackageName : beanPackage) {
                            for (NamingStrategy namingStrategy : ns) {
                                for (boolean withBeans : booleans) {
                                    for (boolean withInnerClasses : booleans) {
                                        for (boolean withOrdinalPositioning : booleans) {
                                            for (boolean exportColumns : booleans) {
                                                for (boolean schemaToPackage : booleans) {
                                                    if (withBeans && beanPackageName == null) {
                                                        if (Objects.equal(namePrefix, beanPrefix) && Objects.equal(nameSuffix, beanSuffix)) {
                                                            continue;
                                                        }
                                                    }

                                                    if (withInnerClasses && withOrdinalPositioning) {
                                                        continue;
                                                    }

                                                    params.add(new Object[]{namePrefix, nameSuffix, beanPrefix,
                                                            beanSuffix, beanPackageName,
                                                            namingStrategy,
                                                            withBeans, withInnerClasses,
                                                            withOrdinalPositioning, exportColumns,
                                                            schemaToPackage});
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return params;
    }

    public MetaDataExporterAllTest(String namePrefix, String nameSuffix, String beanPrefix,
                                   String beanSuffix, String beanPackageName,
                                   NamingStrategy namingStrategy,
                                   boolean withBeans, boolean withInnerClasses,
                                   boolean withOrdinalPositioning, boolean exportColumns,
                                   boolean schemaToPackage) {
        this.namePrefix = namePrefix;
        this.nameSuffix = nameSuffix;
        this.beanPrefix = beanPrefix;
        this.beanSuffix = beanSuffix;
        this.beanPackageName = beanPackageName;
        this.schemaToPackage = schemaToPackage;
        this.namingStrategy = namingStrategy;
        this.withBeans = withBeans;
        this.withInnerClasses = withInnerClasses;
        this.withOrdinalPositioning = withOrdinalPositioning;
        this.exportColumns = exportColumns;
    }

    @Test
    public void export() throws SQLException, IOException {
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setColumnAnnotations(exportColumns);
        exporter.setSchemaPattern("PUBLIC");
        exporter.setNamePrefix(namePrefix);
        exporter.setNameSuffix(nameSuffix);
        exporter.setBeanPrefix(beanPrefix);
        exporter.setBeanSuffix(beanSuffix);
        exporter.setInnerClassesForKeys(withInnerClasses);
        exporter.setPackageName("test");
        exporter.setConfiguration(new Configuration(SQLTemplates.DEFAULT));
        exporter.setBeanPackageName(beanPackageName);
        exporter.setTargetFolder(folder.getRoot());
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
            Assert.fail("Compilation Failed for " + folder.getRoot().getPath());
        }

    }
}
