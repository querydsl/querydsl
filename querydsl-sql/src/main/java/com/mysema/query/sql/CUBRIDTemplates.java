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
 * 
 * @author tiwe
 *
 */
public class CUBRIDTemplates extends SQLTemplates {
    
    private final String limitTemplate = "\nlimit {0}";
    
    private final String offsetLimitTemplate = "\nlimit {0}, {1}";
    
//    private String offsetTemplate = "\noffset {0}";

    public CUBRIDTemplates() {
        this('\\', false);
    }
    
    public CUBRIDTemplates(boolean quote) {
        this('\\',quote);
    }

    public CUBRIDTemplates(char escape, boolean quote) {
        super("\"", escape, quote);
        setParameterMetadataAvailable(false);
        add(Ops.MATCHES, "{0} regexp {1}");
//        setNativeMerge(true);
//        add(Ops.MathOps.ROUND, "round({0},0)");
//        add(Ops.TRIM, "trim(both from {0})");
//        add(Ops.CONCAT, "concat({0},{1})");
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
