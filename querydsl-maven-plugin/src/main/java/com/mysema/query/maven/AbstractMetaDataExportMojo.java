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
package com.mysema.query.maven;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.mysema.query.codegen.BeanSerializer;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.codegen.DefaultNamingStrategy;
import com.mysema.query.sql.codegen.MetaDataExporter;
import com.mysema.query.sql.codegen.NamingStrategy;
import com.mysema.query.sql.types.Type;

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
     * name prefix for query-types (default: "Q")
     * @parameter default-value="Q"
     */
    private String namePrefix;

    /**
     * name prefix for query-types (default: "")
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
     *        <code>null</code> means that the schema name should not be used to narrow
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
     * @parameter default-value=true
     */
    private boolean exportPrimaryKeys;

    /**
     * @parameter default-value=true
     */
    private boolean exportForeignKeys;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isForTest()) {
            project.addTestCompileSourceRoot(targetFolder);
        } else {
            project.addCompileSourceRoot(targetFolder);
        }
        NamingStrategy namingStrategy;
        if (namingStrategyClass != null) {
            try {
                namingStrategy = (NamingStrategy) Class.forName(namingStrategyClass).newInstance();
            } catch (InstantiationException e) {
                throw new MojoExecutionException(e.getMessage(),e);
            } catch (IllegalAccessException e) {
                throw new MojoExecutionException(e.getMessage(),e);
            } catch (ClassNotFoundException e) {
                throw new MojoExecutionException(e.getMessage(),e);
            }
        } else {
            namingStrategy = new DefaultNamingStrategy();
        }

        // defaults for Scala
        if (createScalaSources) {
            if (serializerClass == null) {
                serializerClass = "com.mysema.query.scala.sql.ScalaMetaDataSerializer";
            }
            if (exportBeans && beanSerializerClass == null) {
                beanSerializerClass = "com.mysema.query.scala.ScalaBeanSerializer";
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
        exporter.setExportPrimaryKeys(exportPrimaryKeys);
        exporter.setExportForeignKeys(exportForeignKeys);
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
                try {
                    exporter.setBeanSerializerClass((Class)Class.forName(beanSerializerClass));
                } catch (ClassNotFoundException e) {
                    getLog().error(e);
                    throw new MojoExecutionException(e.getMessage(), e);
                }
            } else {
                exporter.setBeanSerializer(new BeanSerializer());
            }

        }
        String sourceEncoding = (String)project.getProperties().get("project.build.sourceEncoding");
        if (sourceEncoding != null) {
            exporter.setSourceEncoding(sourceEncoding);
        }

        try {
            if (customTypes != null) {
                Configuration configuration = new Configuration(SQLTemplates.DEFAULT);
                for (String cl : customTypes) {
                    configuration.register((Type<?>)Class.forName(cl).newInstance());
                }
                exporter.setConfiguration(configuration);
            }
        } catch (IllegalAccessException e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (InstantiationException e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        }

        try {
            Class.forName(jdbcDriver);
            Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            try{
                exporter.export(conn.getMetaData());
            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (SQLException e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            getLog().error(e);
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

}
