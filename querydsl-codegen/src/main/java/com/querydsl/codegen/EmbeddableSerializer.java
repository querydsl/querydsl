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
package com.querydsl.codegen;

import static com.querydsl.codegen.utils.Symbols.UNCHECKED;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import com.querydsl.codegen.utils.CodeWriter;
import com.querydsl.codegen.utils.model.ClassType;
import com.querydsl.codegen.utils.model.Type;
import com.querydsl.codegen.utils.model.TypeCategory;
import com.querydsl.codegen.utils.model.Types;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.*;

/**
 * {@code EmbeddableSerializer} is a {@link Serializer} implementation for embeddable types
 *
 * @author tiwe
 *
 */
public final class EmbeddableSerializer extends EntitySerializer {

    /**
     * Create a new {@code EmbeddableSerializer} instance
     *
     * @param typeMappings type mappings to be used
     * @param keywords keywords to be used
     * @param generatedAnnotationClass the fully qualified class name of the <em>Single-Element Annotation</em> (with {@code String} element) to be used on the generated classes.
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.3">Single-Element Annotation</a>
     */
    @Inject
    public EmbeddableSerializer(
            TypeMappings typeMappings,
            @Named(CodegenModule.KEYWORDS) Collection<String> keywords,
            @Named(CodegenModule.GENERATED_ANNOTATION_CLASS) Class<? extends Annotation> generatedAnnotationClass) {
        super(typeMappings, keywords, generatedAnnotationClass);
    }

    /**
     * Create a new {@code EmbeddableSerializer} instance.
     *
     * @param typeMappings type mappings to be used
     * @param keywords keywords to be used
     */
    public EmbeddableSerializer(
            TypeMappings typeMappings,
            Collection<String> keywords) {
        this(typeMappings, keywords, GeneratedAnnotationResolver.resolveDefault());
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    protected void introClassHeader(CodeWriter writer, EntityType model) throws IOException {
        Type queryType = typeMappings.getPathType(model, model, true);

        TypeCategory category = model.getOriginalCategory();
        Class<? extends Path> pathType;
        if (model.getProperties().isEmpty()) {
            switch (category) {
                case COMPARABLE : pathType = ComparablePath.class; break;
                case ENUM: pathType = EnumPath.class; break;
                case DATE: pathType = DatePath.class; break;
                case DATETIME: pathType = DateTimePath.class; break;
                case TIME: pathType = TimePath.class; break;
                case NUMERIC: pathType = NumberPath.class; break;
                case STRING: pathType = StringPath.class; break;
                case BOOLEAN: pathType = BooleanPath.class; break;
                default : pathType = BeanPath.class;
            }
        } else {
            pathType = BeanPath.class;
        }

        for (Annotation annotation : model.getAnnotations()) {
            writer.annotation(annotation);
        }

        writer.line("@", generatedAnnotationClass.getSimpleName(), "(\"", getClass().getName(), "\")");

        if (category == TypeCategory.BOOLEAN || category == TypeCategory.STRING) {
            writer.beginClass(queryType, new ClassType(pathType));
        } else {
            writer.beginClass(queryType, new ClassType(category, pathType, model));
        }

        // TODO : generate proper serialVersionUID here
        writer.privateStaticFinal(Types.LONG_P, "serialVersionUID", model.hashCode() + "L");
    }

}
