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
package com.mysema.query._cubrid;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.Connections;
import com.mysema.query.InsertBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.CUBRIDTemplates;
import com.mysema.testutil.Label;

@Label(Target.CUBRID)
public class InsertCUBRIDTest extends InsertBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initCubrid();
    }

    @Before
    public void setUp() throws SQLException {
        templates = new CUBRIDTemplates(){{
            newLineToSingleSpace();
        }};
        super.setUp();
    }

    // http://sourceforge.net/tracker/index.php?func=detail&aid=3513432&group_id=280608&atid=2377440
    @Test
    public void Insert_With_Keys() throws SQLException {}    
    @Test
    public void Insert_With_Keys_Projected() throws SQLException {}    
    @Test
    public void Insert_With_Keys_Projected2() throws SQLException {}
    
}
