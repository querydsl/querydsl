/*
 * Copyright 2013, Mysema Ltd
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

import javax.persistence.Temporal;
import javax.persistence.metamodel.*;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.nio.charset.Charset;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.SerializerConfig;
import com.querydsl.codegen.SimpleSerializerConfig;
import org.hibernate.MappingException;

/**
 * JPADomainExporter exports JPA 2 metamodels to Querydsl expression types
 *
 * @author tiwe
 *
 */
public class JPADomainExporter extends AbstractDomainExporter {

    private final Metamodel configuration;

    /**
     * Create a new JPADomainExporter instance
     *
     * @param targetFolder
     * @param configuration
     */
    public JPADomainExporter(File targetFolder, Metamodel configuration) {
        this("Q", "", targetFolder, SimpleSerializerConfig.DEFAULT, configuration,
                Charset.defaultCharset());
    }

    /**
     * Create a new JPADomainExporter instance
     *
     * @param namePrefix
     * @param targetFolder
     * @param configuration
     */
    public JPADomainExporter(String namePrefix, File targetFolder, Metamodel configuration) {
        this(namePrefix, "", targetFolder, SimpleSerializerConfig.DEFAULT, configuration,
                Charset.defaultCharset());
    }

    /**
     * Create a new JPADomainExporter instance
     *
     * @param namePrefix
     * @param targetFolder
     * @param configuration
     * @param charset
     */
    public JPADomainExporter(String namePrefix, File targetFolder, Metamodel configuration,
            Charset charset) {
        this(namePrefix, "", targetFolder, SimpleSerializerConfig.DEFAULT, configuration, charset);
    }

    /**
     * Create a new JPADomainExporter instance
     *
     * @param namePrefix
     * @param nameSuffix
     * @param targetFolder
     * @param configuration
     */
    public JPADomainExporter(String namePrefix, String nameSuffix, File targetFolder,
            Metamodel configuration) {
        this(namePrefix, nameSuffix, targetFolder, SimpleSerializerConfig.DEFAULT, configuration,
                Charset.defaultCharset());
    }

    /**
     * Create a new JPADomainExporter instance
     *
     * @param namePrefix
     * @param targetFolder
     * @param serializerConfig
     * @param configuration
     */
    public JPADomainExporter(String namePrefix, File targetFolder,
            SerializerConfig serializerConfig, Metamodel configuration) {
        this(namePrefix, "", targetFolder, serializerConfig, configuration, Charset.defaultCharset());
    }

    /**
     * Create a new JPADomainExporter instance
     *
     * @param namePrefix
     * @param targetFolder
     * @param serializerConfig
     * @param configuration
     * @param charset
     */
    public JPADomainExporter(String namePrefix, File targetFolder,
            SerializerConfig serializerConfig, Metamodel configuration, Charset charset) {
        this(namePrefix, "", targetFolder, serializerConfig, configuration, charset);
    }

    /**
     * Create a new JPADomainExporter instance
     *
     * @param namePrefix
     * @param nameSuffix
     * @param targetFolder
     * @param serializerConfig
     * @param configuration
     * @param charset
     */
    public JPADomainExporter(String namePrefix, String nameSuffix, File targetFolder,
            SerializerConfig serializerConfig, Metamodel configuration, Charset charset) {
        super(namePrefix, nameSuffix, targetFolder, serializerConfig, charset);
        this.configuration = configuration;
    }

    @Override
    protected void collectTypes() throws IOException, XMLStreamException, ClassNotFoundException,
        NoSuchMethodException {

        Map<ManagedType<?>, EntityType> types = Maps.newHashMap();
        for (ManagedType<?> managedType : configuration.getManagedTypes()) {
            if (managedType instanceof MappedSuperclassType) {
                types.put(managedType, createSuperType(managedType.getJavaType()));
            } else if (managedType instanceof javax.persistence.metamodel.EntityType) {
                types.put(managedType, createEntityType(managedType.getJavaType()));
            } else if (managedType instanceof EmbeddableType) {
                types.put(managedType, createEmbeddableType(managedType.getJavaType()));
            } else {
                throw new IllegalArgumentException("Unknown type " + managedType);
            }
        }

        // handle properties
        for (Map.Entry<ManagedType<?>, EntityType> entry : types.entrySet()) {
            EntityType entityType = entry.getValue();
            for (Attribute<?,?> attribute : entry.getKey().getDeclaredAttributes()) {
                handleProperty(entityType, entityType.getJavaClass(), attribute);
            }
        }

    }

    private void handleProperty(EntityType entityType, Class<?> cl, Attribute<?,?> p)
            throws NoSuchMethodException, ClassNotFoundException {
        Class<?> clazz = Object.class;
        try {
            clazz = p.getJavaType();
        } catch (MappingException e) {
            // ignore
        }
        Type propertyType = getType(cl, clazz, p.getName());

        AnnotatedElement annotated = getAnnotatedElement(cl, p.getName());
        propertyType = getTypeOverride(propertyType, annotated);
        if (propertyType == null) {
            return;
        }

        if (p.isCollection()) {
            if (p instanceof MapAttribute) {
                MapAttribute<?,?,?> map = (MapAttribute<?,?,?>)p;
                Type keyType = typeFactory.get(map.getKeyJavaType());
                Type valueType = typeFactory.get(map.getElementType().getJavaType());
                valueType = getPropertyType(p, valueType);
                propertyType = new SimpleType(propertyType,
                        normalize(propertyType.getParameters().get(0), keyType),
                        normalize(propertyType.getParameters().get(1), valueType));
            } else {
                Type valueType = typeFactory.get(((PluralAttribute<?,?,?>)p).getElementType().getJavaType());
                valueType = getPropertyType(p, valueType);
                propertyType = new SimpleType(propertyType,
                        normalize(propertyType.getParameters().get(0), valueType));
            }
        } else {
            propertyType = getPropertyType(p, propertyType);
        }

        Property property = createProperty(entityType, p.getName(), propertyType, annotated);
        entityType.addProperty(property);
    }

    private Type getPropertyType(Attribute<?, ?> p, Type propertyType) {
        Temporal temporal = ((AnnotatedElement)p.getJavaMember()).getAnnotation(Temporal.class);
        if (temporal != null) {
            switch (temporal.value()) {
            case DATE: propertyType = propertyType.as(TypeCategory.DATE); break;
            case TIME: propertyType = propertyType.as(TypeCategory.TIME); break;
            case TIMESTAMP: propertyType = propertyType.as(TypeCategory.DATETIME); break;
            }
        }
        return propertyType;
    }

}
