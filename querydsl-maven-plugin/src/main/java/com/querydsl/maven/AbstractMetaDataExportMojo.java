/*
 * Copyright 2011, Mysema Ltd
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

import com.mysema.codegen.model.SimpleType;
import com.querydsl.codegen.BeanSerializer;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.codegen.DefaultNamingStrategy;
import com.querydsl.sql.codegen.MetaDataExporter;
import com.querydsl.sql.codegen.NamingStrategy;
import com.querydsl.sql.types.Type;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * MetaDataExportMojo is a goal for MetaDataExporter usage
 *
 * @author tiwe
 */
public class AbstractMetaDataExportMojo extends AbstractMojo{

    /**
     * @parameter expression="${project}" readonly=true required=true
     */
    private MavenProject project;

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
     * name prefix for querydsl-types (default: "")
     * @parameter default-value=""
     */
    private String nameSuffix;

    /**
     * name prefix for bean types (default: "")
     * @parameter default-value=""
     */
    private String beanPrefix;

    /**
     * name prefix for bean types (default: "")
     * @parameter default-value=""
     */
    private String beanSuffix;

    /**
     * package name for sources
     * @parameter required=true
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
     * tableNamePattern a table name pattern; must match the
    *        table name as it is stored in the database (default: null)
     *
     * @parameter
     */
    private String tableNamePattern;

    /**
     * target source folder to create the sources into (e.g. target/generated-sources/java)
     *
     * @parameter required=true
     */
    private String targetFolder;

    /**
     * namingstrategy class to override (default: DefaultNamingStrategy)
     *
     * @parameter
     */
    private String namingStrategyClass;

    /**
     * @parameter
     */
    private String beanSerializerClass;

    /**
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
     * @parameter
     */
    private String[] beanInterfaces;

    /**
     * @parameter default-value=false
     */
    private boolean beanAddToString;

    /**
     * @parameter default-value=false
     */
    private boolean beanAddFullConstructor;

    /**
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
     * @parameter
     */
    private String[] customTypes;

    /**
     * @parameter
     */
    private TypeMapping[] typeMappings;

    /**
     * @parameter
     */
    private NumericMapping[] numericMappings;

    /**
     * @parameter default-value=false
     */
    private boolean createScalaSources;

    /**
     * @parameter default-value=false
     */
    private boolean schemaToPackage;

    /**
     * @parameter default-value=false
     */
    private boolean lowerCase;

    /**
     * @parameter default-value=true
     */
    private boolean exportTables;

    /**
     * @parameter default-value=true
     */
    private boolean exportViews;

    /**
     * @parameter default-value=false
     */
    private boolean exportAll;

    /**
     * @parameter default-value=true
     */
    private boolean exportPrimaryKeys;

    /**
     * @parameter default-value=true
     */
    private boolean exportForeignKeys;

    /**
     * override default column order (default: alphabetical)
     * 
     * @parameter 
     */
    private String columnComparatorClass;

    /**
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
     * java import added to generated querydsl classes:
     * com.bar for package (without .* notation)
     * com.bar.Foo for class
     *
     * @parameter
     */
    private String[] imports;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isForTest()) {
            project.addTestCompileSourceRoot(targetFolder);
        } else {
            project.addCompileSourceRoot(targetFolder);
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
            if (namePrefix != null) {
                exporter.setNamePrefix(namePrefix);
            }
            if (nameSuffix != null) {
                exporter.setNameSuffix(nameSuffix);
            }
            if (beanPrefix != null) {
                exporter.setBeanPrefix(beanPrefix);
            }
            if (beanSuffix != null) {
                exporter.setBeanSuffix(beanSuffix);
            }
            exporter.setCreateScalaSources(createScalaSources);
            exporter.setPackageName(packageName);
            exporter.setBeanPackageName(beanPackageName);
            exporter.setInnerClassesForKeys(innerClassesForKeys);
            exporter.setTargetFolder(new File(targetFolder));
            exporter.setNamingStrategy(namingStrategy);
            exporter.setSchemaPattern(schemaPattern);
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
            exporter.setSpatial(spatial);

            if (imports != null && imports.length > 0) {
                exporter.setImports(imports);
            }

            if (serializerClass != null) {
                try {
                    exporter.setSerializerClass((Class)Class.forName(serializerClass));
                } catch (ClassNotFoundException e) {
                    getLog().error(e);
                    throw new MojoExecutionException(e.getMessage(), e);
                }
            }
            if (exportBeans) {
                if (beanSerializerClass != null) {
                    exporter.setBeanSerializerClass((Class)Class.forName(beanSerializerClass));
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
            String sourceEncoding = (String)project.getProperties().get("project.build.sourceEncoding");
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
                    Class<?> typeClass = Class.forName(mapping.type);
                    if (Type.class.isAssignableFrom(typeClass)) {
                        configuration.register(mapping.table, mapping.column, (Type<?>)typeClass.newInstance());
                    } else {
                        configuration.register(mapping.table, mapping.column, typeClass);
                    }
                }
            }
            if (numericMappings != null) {
                for (NumericMapping mapping : numericMappings) {
                    int total = Math.max(mapping.size, mapping.total);
                    int decimal = Math.max(mapping.digits, mapping.decimal);
                    configuration.registerNumeric(total, decimal, Class.forName(mapping.javaType));
                }
            }
            if (columnComparatorClass != null) {
                try {
                    exporter.setColumnComparatorClass( (Class) Class.forName(this.columnComparatorClass).asSubclass(Comparator.class));
                } catch (ClassNotFoundException e) {
                    getLog().error(e);
                    throw new MojoExecutionException(e.getMessage(), e);
                }
            }

            exporter.setConfiguration(configuration);

            Class.forName(jdbcDriver);
            Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            try{
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

    public void setImports(String[] imports) {
        this.imports = imports;
    }
}
