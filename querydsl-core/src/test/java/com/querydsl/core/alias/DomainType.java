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
package com.querydsl.core.alias;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DomainType{

    String getFirstName();

    String getLastName();

    int getAge();

    List<DomainType> getList();
    
    Collection<DomainType> getCollection();
    
    Set<DomainType> getSet();

    Map<String,DomainType> getMap();

    BigDecimal getBigDecimal();

    BigInteger getBigInteger();

    Byte getByte();

    Double getDouble();

    Float getFloat();

    java.sql.Date getDate();

    java.util.Date getDate2();

    Short getShort();

    Time getTime();

    Timestamp getTimestamp();

    Gender getGender();

}