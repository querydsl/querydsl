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
package com.querydsl.core.support;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DummyEntity {

    private DummyEntity other;
    
    private List<DummyEntity> list;
    
    private Set<DummyEntity> set;
    
    private Map<String,DummyEntity> map;

    public List<DummyEntity> getList() {
        return list;
    }

    public Set<DummyEntity> getSet() {
        return set;
    }

    public Map<String, DummyEntity> getMap() {
        return map;
    }

    public DummyEntity getOther() {
        return other;
    }
    
}
