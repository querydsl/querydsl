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
package com.mysema.query.codegen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;
import javax.inject.Inject;
import javax.inject.Named;

import com.mysema.codegen.CodeWriter;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.path.SimplePath;

/**
 * SupertypeSerializer is a {@link Serializer} implementation for supertypes
 *
 * @author tiwe
 *
 */
public final class SupertypeSerializer extends EntitySerializer{

    /**
     * Create a new SupertypeSerializer instance
     * 
     * @param typeMappings
     * @param keywords
     */
    @Inject
    public SupertypeSerializer(TypeMappings typeMappings, @Named("keywords") Collection<String> keywords) {
        super(typeMappings, keywords);
    }

//    @Override
//    protected void constructorsForVariables(CodeWriter writer, EntityType model) {
//        // no constructors for variables
//    }
//
//    @Override
//    protected void introDefaultInstance(CodeWriter writer, EntityType model) {
//        // no default instance
//    }
//
//    @Override
//    protected void introFactoryMethods(CodeWriter writer, EntityType model) throws IOException {
//        // no factory methods
//    }

    @Override
    protected void introImports(CodeWriter writer, SerializerConfig config, EntityType model) throws IOException {
        introDelegatePackages(writer, model);

        writer.staticimports(PathMetadataFactory.class);
        
        List<Package> packages = new ArrayList<Package>();
        packages.add(PathMetadata.class.getPackage());
        packages.add(SimplePath.class.getPackage());
        if ((model.hasLists() && config.useListAccessors())
                || !model.getDelegates().isEmpty()
                || (model.hasMaps() && config.useMapAccessors())) {
            packages.add(ComparableExpression.class.getPackage());
        }
        writer.imports(packages.toArray(new Package[packages.size()]));
        
        writer.imports(Generated.class);
    }

}
