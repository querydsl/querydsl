/*
 * Copyright 2012, Mysema Ltd
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
package com.mysema.query.sql;

import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.Ops;

/**
 * CUBRIDTemplates is a SQL dialect for CUBRID
 * 
 * @author tiwe
 *
 */
public class CUBRIDTemplates extends SQLTemplates {
    
    private final String limitTemplate = "\nlimit {0}";
    
    private final String offsetLimitTemplate = "\nlimit {0}, {1}";

    public CUBRIDTemplates() {
        this('\\', false);
    }
    
    public CUBRIDTemplates(boolean quote) {
        this('\\',quote);
    }

    public CUBRIDTemplates(char escape, boolean quote) {
        super("\"", escape, quote);
        setDummyTable(null);
        setParameterMetadataAvailable(false);
        
        add(Ops.MathOps.LN, "ln({0})");
        add(Ops.MathOps.LOG, "(ln({0}) / ln({1}))");
        add(Ops.MathOps.COSH, "(exp({0}) + exp({0} * -1)) / 2");
        add(Ops.MathOps.COTH, "(exp({0} * 2) + 1) / (exp({0} * 2) - 1)");
        add(Ops.MathOps.SINH, "(exp({0}) - exp({0} * -1)) / 2");
        add(Ops.MathOps.TANH, "(exp({0} * 2) - 1) / (exp({0} * 2) + 1)");
        
//        add(Ops.DateTimeOps.DATE_ADD, "date_add({0}, interval {1} {2s})");
    }
    
    protected void serializeModifiers(QueryMetadata metadata, SerializationContext context) {
        QueryModifiers mod = metadata.getModifiers();
        if (mod.getLimit() != null) {
            if (mod.getOffset() != null) {
                context.handle(offsetLimitTemplate, mod.getOffset(), mod.getLimit());
            } else {
                context.handle(limitTemplate, mod.getLimit());    
            }            
        } else if (mod.getOffset() != null) {
            // ?!?
        }
    }
    
}
