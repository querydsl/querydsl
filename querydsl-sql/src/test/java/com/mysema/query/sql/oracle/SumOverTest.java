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
package com.mysema.query.sql.oracle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.NumberPath;

public class SumOverTest {

    @Test
    public void Mutable() {
        NumberPath<Integer> intPath = new NumberPath<Integer>(Integer.class, "intPath");
        SumOver<Integer> sumOver = new SumOver<Integer>(intPath);
        sumOver.orderBy(intPath);
        assertEquals("sum(intPath) over (order by intPath)", sumOver.toString());
        
        assertEquals("sum(intPath) over (partition by intPath order by intPath)", sumOver.partition(intPath).toString());
    }
    
    @Test
    public void ToString() {
        NumberPath<Integer> intPath = new NumberPath<Integer>(Integer.class, "intPath");
        SumOver<Integer> sumOver = new SumOver<Integer>(intPath);
        sumOver.orderBy(intPath);
        sumOver.partition(intPath);

        assertEquals("sum(intPath) over (partition by intPath order by intPath)", sumOver.toString());
    }

    @Test
    public void Equals() {
        NumberPath<Integer> intPath = new NumberPath<Integer>(Integer.class, "intPath");
        SumOver<Integer> sumOver1 = new SumOver<Integer>(intPath).orderBy(intPath).partition(intPath);
        SumOver<Integer> sumOver2 = new SumOver<Integer>(intPath).orderBy(intPath).partition(intPath);
        assertEquals(sumOver1, sumOver2);
    }

}
