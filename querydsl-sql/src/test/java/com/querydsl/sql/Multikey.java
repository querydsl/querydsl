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
package com.querydsl.sql;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Multikey is a Querydsl bean type
 */
//@Schema("PUBLIC")
//@Table("MULTIKEY")
public class Multikey {

    @Column("ID")
    @NotNull
    private Integer id;

    @Column("ID2")
    @NotNull
    @Size
    private String id2;

    @Column("ID3")
    @NotNull
    private Integer id3;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public Integer getId3() {
        return id3;
    }

    public void setId3(Integer id3) {
        this.id3 = id3;
    }

    @Override
    public boolean equals(Object o) {
        if (id == null || id2 == null || id3 == null) {
            return super.equals(o);
        }
        if (!(o instanceof Multikey)) {
            return false;
        }
        Multikey obj = (Multikey)o;
        return id.equals(obj.id) && id2.equals(obj.id2) && id3.equals(obj.id3);
    }

    @Override
    public int hashCode() {
        if (id == null || id2 == null || id3 == null) {
            return super.hashCode();
        }
        final int primary = 31;
        int result = 1;
        result = primary * result + id.hashCode();
        result = primary * result + id2.hashCode();
        result = primary * result + id3.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Multikey#" + id+ ";" + id2+ ";" + id3;
    }

}

