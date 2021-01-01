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
package com.querydsl.jpa.codegen;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.*;

import com.querydsl.codegen.utils.model.SimpleType;
import com.querydsl.codegen.utils.model.Type;
import com.querydsl.codegen.utils.model.TypeCategory;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.SerializerConfig;
import com.querydsl.codegen.SimpleSerializerConfig;

/**
 * {@code HibernateDomainExporter} exports Hibernate XML configuration files to Querydsl expression types
 *
 * @author tiwe
 *
 */
public class HibernateDomainExporter extends AbstractDomainExporter {

    private static final Logger logger = Logger.getLogger(HibernateDomainExporter.class.getName());

    private final Configuration configuration;

    /**
     * Create a new HibernateDomainExporter instance
     *
     * @param targetFolder target folder
     * @param configuration configuration
     */
    public HibernateDomainExporter(File targetFolder, Configuration configuration) {
        this("Q", "", targetFolder, SimpleSerializerConfig.DEFAULT, configuration,
                Charset.defaultCharset());
    }

    /**
     * Create a new HibernateDomainExporter instance
     *
     * @param namePrefix name prefix (default: Q)
     * @param targetFolder target folder
     * @param configuration configuration
     */
    public HibernateDomainExporter(String namePrefix, File targetFolder, Configuration configuration) {
        this(namePrefix, "", targetFolder, SimpleSerializerConfig.DEFAULT, configuration,
                Charset.defaultCharset());
    }

    /**
     * Create a new HibernateDomainExporter instance
     *
     * @param namePrefix name prefix (default: Q)
     * @param targetFolder target folder
     * @param configuration configuration
     * @param charset charset (default: system charset)
     */
    public HibernateDomainExporter(String namePrefix, File targetFolder, Configuration configuration,
            Charset charset) {
        this(namePrefix, "", targetFolder, SimpleSerializerConfig.DEFAULT, configuration, charset);
    }

    /**
     * Create a new HibernateDomainExporter instance
     *
     * @param namePrefix name prefix (default: Q)
     * @param nameSuffix name suffix (default: empty)
     * @param targetFolder target folder
     * @param configuration configuration
     */
    public HibernateDomainExporter(String namePrefix, String nameSuffix, File targetFolder,
            Configuration configuration) {
        this(namePrefix, nameSuffix, targetFolder, SimpleSerializerConfig.DEFAULT, configuration,
                Charset.defaultCharset());
    }

    /**
     * Create a new HibernateDomainExporter instance
     *
     * @param namePrefix name prefix (default Q)
     * @param targetFolder target golder
     * @param serializerConfig serializer config
     * @param configuration configuration
     */
    public HibernateDomainExporter(String namePrefix, File targetFolder,
            SerializerConfig serializerConfig, Configuration configuration) {
        this(namePrefix, "", targetFolder, serializerConfig, configuration, Charset.defaultCharset());
    }

    /**
     * Create a new HibernateDomainExporter instance
     *
     * @param namePrefix name prefix (default: Q)
     * @param targetFolder target folder
     * @param serializerConfig serializer config
     * @param configuration configuration
     * @param charset charset (default: system charset)
     */
    public HibernateDomainExporter(String namePrefix, File targetFolder,
            SerializerConfig serializerConfig, Configuration configuration, Charset charset) {
        this(namePrefix, "", targetFolder, serializerConfig, configuration, charset);
    }

    /**
     * Create a new HibernateDomainExporter instance
     *
     * @param namePrefix name prefix (default: Q)
     * @param nameSuffix name suffix (default: empty)
     * @param targetFolder target folder
     * @param serializerConfig serializer config
     * @param configuration configuration
     * @param charset charset (default: system charset)
     */
    public HibernateDomainExporter(String namePrefix, String nameSuffix, File targetFolder,
            SerializerConfig serializerConfig, Configuration configuration, Charset charset) {
        super(namePrefix, nameSuffix, targetFolder, serializerConfig, charset);
        configuration.buildMappings();
        this.configuration = configuration;
    }

    @Override
    protected void collectTypes() throws IOException, XMLStreamException, ClassNotFoundException,
        NoSuchMethodException {
        // super classes
        Iterator<?> superClassMappings = configuration.getMappedSuperclassMappings();
        while (superClassMappings.hasNext()) {
            MappedSuperclass msc = (MappedSuperclass) superClassMappings.next();
            EntityType entityType = createSuperType(msc.getMappedClass());
            if (msc.getDeclaredIdentifierProperty() != null) {
                handleProperty(entityType, msc.getMappedClass(), msc.getDeclaredIdentifierProperty());
            }
            Iterator<?> properties = msc.getDeclaredPropertyIterator();
            while (properties.hasNext()) {
                handleProperty(entityType, msc.getMappedClass(), (org.hibernate.mapping.Property) properties.next());
            }
        }

        // entity classes
        Iterator<?> classMappings = configuration.getClassMappings();
        while (classMappings.hasNext()) {
            PersistentClass pc = (PersistentClass) classMappings.next();
            EntityType entityType = createEntityType(pc.getMappedClass());
            if (pc.getDeclaredIdentifierProperty() != null) {
                handleProperty(entityType, pc.getMappedClass(), pc.getDeclaredIdentifierProperty());
            } else if (!pc.isInherited() && pc.hasIdentifierProperty()) {
                logger.info(entityType.toString() + pc.getIdentifierProperty());
                handleProperty(entityType, pc.getMappedClass(), pc.getIdentifierProperty());
            } else if (pc.getIdentifier() != null) {
                KeyValue identifier = pc.getIdentifier();
                if (identifier instanceof Component) {
                    Component component = (Component) identifier;
                    Iterator<?> properties = component.getPropertyIterator();
                    if (component.isEmbedded()) {
                        while (properties.hasNext()) {
                            handleProperty(entityType, pc.getMappedClass(), (org.hibernate.mapping.Property) properties.next());
                        }
                    } else {
                        String name = component.getNodeName();
                        Class<?> clazz = component.getType().getReturnedClass();
                        Type propertyType = getType(pc.getMappedClass(), clazz, name);
                        AnnotatedElement annotated = getAnnotatedElement(pc.getMappedClass(), name);
                        Property property = createProperty(entityType, name, propertyType, annotated);
                        entityType.addProperty(property);
                        // handle component properties
                        EntityType embeddedType = createEmbeddableType(propertyType);
                        while (properties.hasNext()) {
                            handleProperty(embeddedType, clazz, (org.hibernate.mapping.Property) properties.next());
                        }
                    }
                }
                // TODO handle other KeyValue subclasses such as Any, DependentValue and ToOne
            }
            Iterator<?> properties = pc.getDeclaredPropertyIterator();
            while (properties.hasNext()) {
                handleProperty(entityType, pc.getMappedClass(), (org.hibernate.mapping.Property) properties.next());
            }
        }
    }

    private void handleProperty(EntityType entityType, Class<?> cl, org.hibernate.mapping.Property p)
            throws NoSuchMethodException, ClassNotFoundException {
        if (p.isBackRef()) {
            return;
        }
        Class<?> clazz = Object.class;
        try {
            clazz = p.getType().getReturnedClass();
        } catch (MappingException e) {
            // ignore
        }
        Type propertyType = getType(cl, clazz, p.getName());
        try {
            propertyType = getPropertyType(p, propertyType);
        } catch (MappingException e) {
            // ignore
        }

        AnnotatedElement annotated = getAnnotatedElement(cl, p.getName());
        propertyType = getTypeOverride(propertyType, annotated);
        if (propertyType == null) {
            return;
        }

        if (p.isComposite()) {
            EntityType embeddedType = createEmbeddableType(propertyType);
            Iterator<?> properties = ((Component) p.getValue()).getPropertyIterator();
            while (properties.hasNext()) {
                handleProperty(embeddedType, embeddedType.getJavaClass(), (org.hibernate.mapping.Property) properties.next());
            }
            propertyType = embeddedType;
        } else if (propertyType.getCategory() == TypeCategory.ENTITY || p.getValue() instanceof ManyToOne) {
            propertyType = createEntityType(propertyType);
        } else if (propertyType.getCategory() == TypeCategory.CUSTOM) {
            propertyType = createEmbeddableType(propertyType);
        } else if (p.getValue() instanceof org.hibernate.mapping.Collection) {
            org.hibernate.mapping.Collection collection = (org.hibernate.mapping.Collection) p.getValue();
            if (collection.getElement() instanceof OneToMany) {
                String entityName = ((OneToMany) collection.getElement()).getReferencedEntityName();
                if (entityName != null) {
                    if (collection.isMap()) {
                        Type keyType = typeFactory.get(Class.forName(propertyType.getParameters().get(0).getFullName()));
                        Type valueType = typeFactory.get(Class.forName(entityName));
                        propertyType = new SimpleType(propertyType,
                                normalize(propertyType.getParameters().get(0), keyType),
                                normalize(propertyType.getParameters().get(1), valueType));
                    } else {
                        Type componentType = typeFactory.get(Class.forName(entityName));
                        propertyType = new SimpleType(propertyType,
                                normalize(propertyType.getParameters().get(0), componentType));
                    }
                }
            } else if (collection.getElement() instanceof Component) {
                Component component = (Component) collection.getElement();
                Class<?> embeddedClass = Class.forName(component.getComponentClassName());
                EntityType embeddedType = createEmbeddableType(embeddedClass);
                Iterator<?> properties = component.getPropertyIterator();
                while (properties.hasNext()) {
                    handleProperty(embeddedType, embeddedClass, (org.hibernate.mapping.Property) properties.next());
                }
            }
        }

        Property property = createProperty(entityType, p.getName(), propertyType, annotated);
        entityType.addProperty(property);
    }

    private Type getPropertyType(org.hibernate.mapping.Property p, Type propertyType) {
        switch (propertyType.getCategory()) {
        case DATE:
        case TIME:
        case DATETIME:
            String type = p.getType().getName();
            if ("time".equals(type)) {
                propertyType = propertyType.as(TypeCategory.TIME);
            } else if ("date".equals(type)) {
                propertyType = propertyType.as(TypeCategory.DATE);
            } else if ("timestamp".equals(type)) {
                propertyType = propertyType.as(TypeCategory.DATETIME);
            }
        default:
        }
        return propertyType;
    }

}
