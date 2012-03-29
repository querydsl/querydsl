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

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.jpa.domain.sql.SAnimal;
import com.mysema.query.sql.MySQLTemplates;


public class NativeSQLSerializerTest {
    
    @Test
    @Ignore
    public void In() {
        NativeSQLSerializer serializer = new NativeSQLSerializer(new MySQLTemplates());
        DefaultQueryMetadata md = new DefaultQueryMetadata();
        SAnimal cat = SAnimal.animal;
        md.addJoin(JoinType.DEFAULT, cat);
        md.addWhere(cat.name.in("X", "Y"));
        md.addProjection(cat.id);
        serializer.serialize(md, false);
        assertEquals("cat.*.id in ?1", serializer.toString());        
    }

}
