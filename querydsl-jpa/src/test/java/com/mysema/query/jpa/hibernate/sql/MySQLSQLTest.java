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
package com.mysema.query.jpa.hibernate.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.testutil.HibernateConfig;
import com.mysema.testutil.HibernateTestRunner;

@Ignore
@RunWith(HibernateTestRunner.class)
@HibernateConfig("mysql.properties")
public class MySQLSQLTest extends AbstractSQLTest {
    
//    @Table("Animal")
    public class SAnimal extends RelationalPathBase<SAnimal> {

        private static final long serialVersionUID = 1L;
        
        public final NumberPath<Integer> id = createNumber("id", Integer.class);
        
        public SAnimal(String variable) {
            super(SAnimal.class, forVariable(variable), null, "Animal");
        }

    }       

    @Override
    @Test
    public void Count() {
        query().from(new SAnimal("cat")).count();        
        query().from(new SAnimal("cat")).countDistinct();
    }
    
}

