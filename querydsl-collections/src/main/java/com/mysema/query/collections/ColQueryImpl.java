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
package com.mysema.query.collections;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;

/**
 * ColQueryImpl is the default implementation of the ColQuery interface
 *
 * @author tiwe
 *
 */
public class ColQueryImpl extends AbstractColQuery<ColQueryImpl> implements ColQuery, Cloneable {

    /**
     * Create a new ColQueryImpl instance
     */
    public ColQueryImpl() {
        super(new DefaultQueryMetadata(), QueryEngine.DEFAULT);
    }

    /**
     * Creates a new ColQueryImpl instance
     * 
     * @param templates
     */
    public ColQueryImpl(ColQueryTemplates templates) {
        this(new DefaultQueryEngine(new DefaultEvaluatorFactory(ColQueryTemplates.DEFAULT)));
    }
    
    /**
     * Create a new ColQueryImpl instance
     *
     * @param evaluatorFactory
     */
    public ColQueryImpl(QueryEngine queryEngine) {
        super(new DefaultQueryMetadata(), queryEngine);
    }
    

    /**
     * Create a new ColQueryImpl instance
     *
     * @param metadata
     * @param evaluatorFactory
     */
    public ColQueryImpl(QueryMetadata metadata) {
        super(metadata, QueryEngine.DEFAULT);
    }

    /**
     * Create a new ColQueryImpl instance
     *
     * @param metadata
     * @param evaluatorFactory
     */
    public ColQueryImpl(QueryMetadata metadata, QueryEngine queryEngine) {
        super(metadata, queryEngine);
    }

    /**
     * Clone the state of this query to a new ColQueryImpl instance
     */
    @Override
    public ColQueryImpl clone() {
        return new ColQueryImpl(queryMixin.getMetadata(), getQueryEngine());
    }

    /**
     * @return
     */
    @Override
    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

}
