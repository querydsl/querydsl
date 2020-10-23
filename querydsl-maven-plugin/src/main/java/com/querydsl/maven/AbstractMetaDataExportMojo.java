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
package com.querydsl.maven;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.wagon.authentication.AuthenticationInfo;

import com.google.common.base.Strings;
import com.mysema.codegen.model.SimpleType;
import com.querydsl.codegen.BeanSerializer;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.codegen.DefaultNamingStrategy;
import com.querydsl.sql.codegen.MetaDataExporter;
import com.querydsl.sql.codegen.NamingStrategy;
import com.querydsl.sql.codegen.support.NumericMapping;
import com.querydsl.sql.codegen.support.RenameMapping;
import com.querydsl.sql.codegen.support.TypeMapping;
import com.querydsl.sql.types.Type;

/**
 * {@code AbstractMetaDataExportMojo} is the base class for {@link MetaDataExporter} usage
 *
 * @author tiwe
 */
public class AbstractMetaDataExportMojo extends AbstractMojo {

    /**
     * maven project
     *
     * @parameter default-value="${project}"
     * @readonly
     */
    private MavenProject project;

    /**
     * The Maven Wagon manager to use when obtaining server authentication details.
     * @component
     */
    private WagonManager wagonManager;

    /**
     * The server id in settings.xml to use as an alternative to jdbcUser and jdbcPassword
     * @parameter
     */
    private String server;

    /**
     * JDBC driver class name
     * @parameter required=true
     */
    private String jdbcDriver;

    /**
     * JDBC connection url
     * @parameter required=true
     */
    private String jdbcUrl;

    /**
     * JDBC connection username
     * @parameter
     */
    private String jdbcUser;

    /**
     * JDBC connection password
     * @parameter
     */
    private String jdbcPassword;

    /**
     * name prefix for querydsl-types (default: "Q")
     * @parameter default-value="Q"
     */
    private String namePrefix;

    /**
     * name suffix for querydsl-types (default: "")
     * @parameter default-value=""
     */
    private String nameSuffix;

    /**
     * name prefix for bean types (default: "")
     * @parameter default-value=""
     */
    private String beanPrefix;

    /**
     * name suffix for bean types (default: "")
     * @parameter default-value=""
     */
    private String beanSuffix;

    /**
     * package name for sources
     * @parameter
     * @required
     */
    private String packageName;

    /**
     * package name for bean sources (default: packageName)
     * @parameter
     */
    private String beanPackageName;

    /**
     * schemaPattern a schema name pattern; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        {@code null} means that the schema name should not be used to narrow
     *        the search (default: null)
     *
     * @parameter
     */
    private String schemaPattern;

    /**
     * a catalog name; must match the catalog name as it
     *      is stored in the database; "" retrieves those without a catalog;
     *      <code>null</code> means that the catalog name should not be used to narrow
     *      the search
     */
    private String catalogPattern;

    /**
     * tableNamePattern a table name pattern; must match the
    *        table name as it is stored in the database (default: null)
     *
     * @parameter
     */
    private String tableNamePattern;

    /**
     * target source folder to create the sources into (e.g. target/generated-sources/java)
     *
     * @parameter
     * @required
     */
    private String targetFolder;

    /**
     * target source folder to create the bean sources into
     *
     * @parameter
     */
    private String beansTargetFolder;

    /**
     * namingstrategy class to override (default: DefaultNamingStrategy)
     *
     * @parameter
     */
    private String namingStrategyClass;

    /**
     * name for bean serializer class
     *
     * @parameter
     */
    private String beanSerializerClass;

    /**
     * name for serializer class
     *
     * @parameter
     */
    private String serializerClass;

    /**
     * serialize beans as well
     *
     * @parameter default-value=false
     */
    private boolean exportBeans;

    /**
     * additional interfaces to be implemented by beans
     *
     * @parameter
     */
    private String[] beanInterfaces;

    /**
     * switch for {@code toString} addition
     *
     * @parameter default-value=false
     */
    private boolean beanAddToString;

    /**
     * switch for full constructor addition
     *
     * @parameter default-value=false
     */
    private boolean beanAddFullConstructor;

    /**
     * switch to print supertype content
     *
     * @parameter default-value=false
     */
    private boolean beanPrintSupertype;

    /**
     * wrap key properties into inner classes (default: false)
     *
     * @parameter default-value=false
     */
    private boolean innerClassesForKeys;

    /**
     * export validation annotations (default: false)
     *
     * @parameter default-value=false
     */
    private boolean validationAnnotations;

    /**
     * export column annotations (default: false)
     *
     * @parameter default-value=false
     */
    private boolean columnAnnotations;

    /**
     * custom type classnames to use
     *
     * @parameter
     */
    private String[] customTypes;

    /**
     * custom type mappings to use
     *
     * @parameter
     */
    private TypeMapping[] typeMappings;

    /**
     * custom numeric mappings
     *
     * @parameter
     */
    private NumericMapping[] numericMappings;

    /**
     * custom rename mappings
     *
     * @parameter
     */
    private RenameMapping[] renameMappings;

    /**
     * switch for generating scala sources
     *
     * @parameter default-value=false
     */
    private boolean createScalaSources;

    /**
     * switch for using schema as suffix in package generation, full package name will be
     * {@code ${packageName}.${schema}}
     *
     * @parameter default-value=false
     */
    private boolean schemaToPackage;

    /**
     * switch to normalize schema, table and column names to lowercase
     *
     * @parameter default-value=false
     */
    private boolean lowerCase;

    /**
     * switch to export tables
     *
     * @parameter default-value=true
     */
    private boolean exportTables;

    /**
     * switch to export views
     *
     * @parameter default-value=true
     */
    private boolean exportViews;

    /**
     * switch to export all types
     *
     * @parameter default-value=false
     */
    private boolean exportAll;

    /**
     * switch to export primary keys
     *
     * @parameter default-value=true
     */
    private boolean exportPrimaryKeys;

    /**
     * switch to export foreign keys
     *
     * @parameter default-value=true
     */
    private boolean exportForeignKeys;

    /**
     * switch to export direct foreign keys
     *
     * @parameter default-value=true
     */
    private boolean exportDirectForeignKeys;

    /**
     * switch to export inverse foreign keys
     *
     * @parameter default-value=true
     */
    private boolean exportInverseForeignKeys;

    /**
     * override default column order (default: alphabetical)
     *
     * @parameter
     */
    private String columnComparatorClass;

    /**
     * switch to enable spatial type support
     *
     * @parameter default-value=false
     */
    private boolean spatial;

    /**
     * Comma-separated list of table types to export (allowable values will
     * depend on JDBC driver). Allows for arbitrary set of types to be exported,
     * e.g.: "TABLE, MATERIALIZED VIEW". The exportTables and exportViews
     * parameters will be ignored if this parameter is set. (default: none)
     *
     * @parameter
     */
    private String tableTypesToExport;

    /**
     * java import added to generated query classes:
     * com.bar for package (without .* notation)
     * com.bar.Foo for class
     *
     * @parameter
     */
    private String[] imports;

    /**
     * Whether to skip the exporting execution
     *
     * @parameter default-value=false property="maven.querydsl.skip"
     */
    private boolean skip;

    /**
     * The fully qualified class name of the <em>Single-Element Annotation</em> (with <code>String</code> element) to put on the generated sources.Defaults to
     * <code>javax.annotation.Generated</code> or <code>javax.annotation.processing.Generated</code> depending on the java version.
     * <em>See also</em> <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.3">Single-Element Annotation</a>
     *
     * @parameter
     */
    private String generatedAnnotationClass;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isForTest()) {
            project.addTestCompileSourceRoot(targetFolder);
        } else {
            project.addCompileSourceRoot(targetFolder);
        }

        if (skip) {
            return;
        }

        try {
            Configuration configuration = new Configuration(SQLTemplates.DEFAULT);
            NamingStrategy namingStrategy;
            if (namingStrategyClass != null) {
                namingStrategy = (NamingStrategy) Class.forName(namingStrategyClass).newInstance();
            } else {
                namingStrategy = new DefaultNamingStrategy();
            }

            // defaults for Scala
            if (createScalaSources) {
                if (serializerClass == null) {
                    serializerClass = "com.querydsl.scala.sql.ScalaMetaDataSerializer";
                }
                if (exportBeans && beanSerializerClass == null) {
                    beanSerializerClass = "com.querydsl.scala.ScalaBeanSerializer";
                }
            }

            MetaDataExporter exporter = new MetaDataExporter();
            exporter.setNamePrefix(emptyIfSetToBlank(namePrefix));
            exporter.setNameSuffix(Strings.nullToEmpty(nameSuffix));
            exporter.setBeanPrefix(Strings.nullToEmpty(beanPrefix));
            exporter.setBeanSuffix(Strings.nullToEmpty(beanSuffix));
            if (beansTargetFolder != null) {
                exporter.setBeansTargetFolder(new File(beansTargetFolder));
            }
            exporter.setCreateScalaSources(createScalaSources);
            exporter.setPackageName(packageName);
            exporter.setBeanPackageName(beanPackageName);
            exporter.setInnerClassesForKeys(innerClassesForKeys);
            exporter.setTargetFolder(new File(targetFolder));
            exporter.setNamingStrategy(namingStrategy);
            exporter.setCatalogPattern(catalogPattern);
            exporter.setSchemaPattern(processBlankValues(schemaPattern));
            exporter.setTableNamePattern(tableNamePattern);
            exporter.setColumnAnnotations(columnAnnotations);
            exporter.setValidationAnnotations(validationAnnotations);
            exporter.setSchemaToPackage(schemaToPackage);
            exporter.setLowerCase(lowerCase);
            exporter.setExportTables(exportTables);
            exporter.setExportViews(exportViews);
            exporter.setExportAll(exportAll);
            exporter.setTableTypesToExport(tableTypesToExport);
            exporter.setExportPrimaryKeys(exportPrimaryKeys);
            exporter.setExportForeignKeys(exportForeignKeys);
            exporter.setExportDirectForeignKeys(exportDirectForeignKeys);
            exporter.setExportInverseForeignKeys(exportInverseForeignKeys);
            exporter.setSpatial(spatial);
            exporter.setGeneratedAnnotationClass(generatedAnnotationClass);

            if (imports != null && imports.length > 0) {
                exporter.setImports(imports);
            }

            if (serializerClass != null) {
                try {
                    exporter.setSerializerClass((Class) Class.forName(serializerClass));
                } catch (ClassNotFoundException e) {
                    getLog().error(e);
                    throw new MojoExecutionException(e.getMessage(), e);
                }
            }
            if (exportBeans) {
                if (beanSerializerClass != null) {
                    exporter.setBeanSerializerClass((Class) Class.forName(beanSerializerClass));
                } else {
                    BeanSerializer serializer = new BeanSerializer();
                    if (beanInterfaces != null) {
                        for (String iface : beanInterfaces) {
                            int sepIndex = iface.lastIndexOf('.');
                            if (sepIndex < 0) {
                                serializer.addInterface(new SimpleType(iface));
                            } else {
                                String packageName = iface.substring(0, sepIndex);
                                String simpleName = iface.substring(sepIndex + 1);
                                serializer.addInterface(new SimpleType(iface, packageName, simpleName));
                            }
                        }
                    }
                    serializer.setAddFullConstructor(beanAddFullConstructor);
                    serializer.setAddToString(beanAddToString);
                    serializer.setPrintSupertype(beanPrintSupertype);
                    exporter.setBeanSerializer(serializer);
                }

            }
            String sourceEncoding = (String) project.getProperties().get("project.build.sourceEncoding");
            if (sourceEncoding != null) {
                exporter.setSourceEncoding(sourceEncoding);
            }

            if (customTypes != null) {
                for (String cl : customTypes) {
                    configuration.register((Type<?>) Class.forName(cl).newInstance());
                }
            }
            if (typeMappings != null) {
                for (TypeMapping mapping : typeMappings) {
                    mapping.apply(configuration);
                }
            }
            if (numericMappings != null) {
                for (NumericMapping mapping : numericMappings) {
                    mapping.apply(configuration);
                }
            }
            if (renameMappings != null) {
                for (RenameMapping mapping : renameMappings) {
                    mapping.apply(configuration);
                }
            }

            if (columnComparatorClass != null) {
                try {
                    exporter.setColumnComparatorClass((Class) Class.forName(this.columnComparatorClass).asSubclass(Comparator.class));
                } catch (ClassNotFoundException e) {
                    getLog().error(e);
                    throw new MojoExecutionException(e.getMessage(), e);
                }
            }

            exporter.setConfiguration(configuration);

            Class.forName(jdbcDriver);
            String user;
            String password;
            if (server == null) {
                user = jdbcUser;
                password = jdbcPassword;
            } else {
                AuthenticationInfo info = wagonManager.getAuthenticationInfo(server);
                if (info == null) {
                    throw new MojoExecutionException("No authentication info for server " + server);
                }

                user = info.getUserName();
                if (user == null) {
                    throw new MojoExecutionException("Missing username from server " + server);
                }

                password = info.getPassword();
                if (password == null) {
                    throw new MojoExecutionException("Missing password from server " + server);
                }
            }
            Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
            try {
                exporter.export(conn.getMetaData());
            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (ClassNotFoundException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

    protected boolean isForTest() {
        return false;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public void setJdbcUser(String jdbcUser) {
        this.jdbcUser = jdbcUser;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public void setBeanInterfaces(String[] beanInterfaces) {
        this.beanInterfaces = beanInterfaces;
    }

    public void setBeanPrefix(String beanPrefix) {
        this.beanPrefix = beanPrefix;
    }

    public void setBeanSuffix(String beanSuffix) {
        this.beanSuffix = beanSuffix;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setBeanPackageName(String beanPackageName) {
        this.beanPackageName = beanPackageName;
    }

    public void setCatalogPattern(String catalogPattern) {
        this.catalogPattern = catalogPattern;
    }

    public void setSchemaPattern(String schemaPattern) {
        this.schemaPattern = schemaPattern;
    }

    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

    public void setNamingStrategyClass(String namingStrategyClass) {
        this.namingStrategyClass = namingStrategyClass;
    }

    public void setBeanSerializerClass(String beanSerializerClass) {
        this.beanSerializerClass = beanSerializerClass;
    }

    public void setSerializerClass(String serializerClass) {
        this.serializerClass = serializerClass;
    }

    public void setExportBeans(boolean exportBeans) {
        this.exportBeans = exportBeans;
    }

    public void setInnerClassesForKeys(boolean innerClassesForKeys) {
        this.innerClassesForKeys = innerClassesForKeys;
    }

    public void setValidationAnnotations(boolean validationAnnotations) {
        this.validationAnnotations = validationAnnotations;
    }

    public void setColumnAnnotations(boolean columnAnnotations) {
        this.columnAnnotations = columnAnnotations;
    }

    public void setCustomTypes(String[] customTypes) {
        this.customTypes = customTypes;
    }

    public void setCreateScalaSources(boolean createScalaSources) {
        this.createScalaSources = createScalaSources;
    }

    public void setSchemaToPackage(boolean schemaToPackage) {
        this.schemaToPackage = schemaToPackage;
    }

    public void setLowerCase(boolean lowerCase) {
        this.lowerCase = lowerCase;
    }

    public void setTypeMappings(TypeMapping[] typeMappings) {
        this.typeMappings = typeMappings;
    }

    public void setNumericMappings(NumericMapping[] numericMappings) {
        this.numericMappings = numericMappings;
    }

    public void setRenameMappings(RenameMapping[] renameMappings) {
        this.renameMappings = renameMappings;
    }

    public void setImports(String[] imports) {
        this.imports = imports;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public void setGeneratedAnnotationClass(@Nullable String generatedAnnotationClass) {
        this.generatedAnnotationClass = generatedAnnotationClass;
    }

    private static String emptyIfSetToBlank(String value) {
        boolean setToBlank = value == null || value.equalsIgnoreCase("BLANK");
        return setToBlank ? "" : value;
    }

    private static String processBlankValues(String value) {
        if (value == null) {
            return null;
        }
        return BLANK_VALUE_PATTERN.matcher(value).replaceAll(BLANK_VALUE_REPLACEMENT);
    }

    private static final Pattern BLANK_VALUE_PATTERN = Pattern.compile("(^|,)BLANK(,|$)", Pattern.CASE_INSENSITIVE);
    private static final String BLANK_VALUE_REPLACEMENT = "$1$2";
}
