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
package com.mysema.query._h2;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.BeanPopulationBaseTest;
import com.mysema.query.Connections;
import com.mysema.query.Target;
import com.mysema.query.sql.H2Templates;
import com.mysema.testutil.Label;

@Label(Target.H2)
public class BeanPopulationH2Test extends BeanPopulationBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initH2();
    }

    @Before
    public void setUp() throws SQLException {
        templates = new H2Templates(){{
            newLineToSingleSpace();
        }};
    }
    
}
