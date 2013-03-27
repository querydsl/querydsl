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
package com.mysema.query.jpa;

import com.mysema.query.types.Ops;

/**
 * EclipseLinkTemplates extends JPQLTemplates with EclipseLink specific extensions
 *
 * @author tiwe
 *
 */
public class EclipseLinkTemplates extends JPQLTemplates {
    
    private static final QueryHandler QUERY_HANDLER;
    
    static {
        QueryHandler instance;
        try {
            instance = (QueryHandler) Class.forName("com.mysema.query.jpa.EclipseLinkHandler").newInstance();
        } catch (NoClassDefFoundError e) {
            instance = DefaultQueryHandler.DEFAULT;
        } catch (Exception e) {
            instance = DefaultQueryHandler.DEFAULT;
        }
        QUERY_HANDLER = instance;
    }


    public static final JPQLTemplates DEFAULT = new EclipseLinkTemplates();

    public EclipseLinkTemplates() {
        this(DEFAULT_ESCAPE);
    }
    
    public EclipseLinkTemplates(char escape) {
        super(escape, QUERY_HANDLER);        
        add(Ops.STRING_CAST, "cast({0} as varchar)");        
        add(Ops.CHAR_AT, "substring({0},{1}+1,1)");

    }
    
    @Override
    public boolean isPathInEntitiesSupported() {
        return false;
    }
    
    @Override
    public boolean isSelect1Supported() {
        return true;
    }
    
    
}
