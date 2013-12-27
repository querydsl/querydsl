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
package com.mysema.query.apt.dynamo;

import java.lang.annotation.Annotation;
import java.util.Collections;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.mysema.query.annotations.QueryEntities;
import com.mysema.query.apt.AbstractQuerydslProcessor;
import com.mysema.query.apt.Configuration;
import com.mysema.query.apt.DefaultConfiguration;

/**
 * Annotation processor to create Querydsl query types for DynamoDB mapped classes
 *
 * @author Marvin
 */
@SupportedAnnotationTypes({"com.mysema.query.annotations.*","com.amazonaws.services.dynamodbv2.datamodeling.*"})
public class DynamoAnnotationProcessor extends AbstractQuerydslProcessor {
    
    @Override
    protected Configuration createConfiguration(RoundEnvironment roundEnv) {
        Class<? extends Annotation> entities = QueryEntities.class;
        Class<? extends Annotation> entity = DynamoDBTable.class;
        Class<? extends Annotation> skip = DynamoDBIgnore.class;
        DefaultConfiguration conf = new DefaultConfiguration(roundEnv, processingEnv.getOptions(), Collections.<String>emptySet(), 
                entities, entity, null, null, null, skip);
        return conf;
    }

}
