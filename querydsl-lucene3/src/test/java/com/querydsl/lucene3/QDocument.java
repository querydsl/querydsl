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
package com.querydsl.lucene3;

import org.apache.lucene.document.Document;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;

public class QDocument extends EntityPathBase<Document> {

    private static final long serialVersionUID = -4872833626508344081L;

    public QDocument(final String var) {
        super(Document.class, PathMetadataFactory.forVariable(var));
    }

    public final NumberPath<Integer> year = createNumber("year", Integer.class);
    
    public final StringPath title = createString("title");
    
    public final NumberPath<Double> gross = createNumber("gross", Double.class);
    
}