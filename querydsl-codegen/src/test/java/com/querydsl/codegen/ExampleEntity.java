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
package com.querydsl.codegen;

import java.util.List;
import java.util.Map;

import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public class ExampleEntity extends ExampleSupertype {

    private String name;

    private ExampleEntity mate;

    private List<ExampleEntity> mates;

    private Map<String, ExampleEntity> matesByName;

    private ExampleEmbeddable embeddable;

    @QueryEmbedded
    private ExampleEmbedded embedded;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExampleEntity getMate() {
        return mate;
    }

    public void setMate(ExampleEntity mate) {
        this.mate = mate;
    }

    public ExampleEmbeddable getEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(ExampleEmbeddable embeddable) {
        this.embeddable = embeddable;
    }

    public List<ExampleEntity> getMates() {
        return mates;
    }

    public void setMates(List<ExampleEntity> mates) {
        this.mates = mates;
    }

    public Map<String, ExampleEntity> getMatesByName() {
        return matesByName;
    }

    public void setMatesByName(Map<String, ExampleEntity> matesByName) {
        this.matesByName = matesByName;
    }

    public ExampleEmbedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(ExampleEmbedded embedded) {
        this.embedded = embedded;
    }



}
