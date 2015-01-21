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
package com.querydsl.jpa;

import org.junit.Test;

import com.querydsl.jpa.domain.QShow;

public class MapTest extends AbstractQueryTest {

    private QShow show = QShow.show;
    
    @Test
    public void Contains() {
        assertToString("show.acts[?1] = ?2", show.acts.contains("x", "y"));
    }
    
    @Test
    public void Contains_Key() {
        assertToString("?1 in indices(show.acts)", show.acts.containsKey("x"));
    }
    
    @Test
    public void Contains_Value() {
        assertToString("?1 in elements(show.acts)", show.acts.containsValue("y"));
    }
}
