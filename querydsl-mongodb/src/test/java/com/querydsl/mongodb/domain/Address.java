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
package com.querydsl.mongodb.domain;

import org.mongodb.morphia.annotations.Embedded;

public final class Address {

    public Address() {
        
    }
    
    public Address(String street, String postCode, City city) {
        this.street = street; this.postCode = postCode; this.city = city;
    }
    
    public String street;
    
    public String postCode;
    
    @Embedded
    public City city;
    
}
