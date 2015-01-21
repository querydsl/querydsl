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
package com.querydsl.jdo.test.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.*;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@FetchGroups({
    @FetchGroup(name="products", members={@Persistent(name="products")})
})
public class Store {

    private String name;

    @Join
    @Element(types=Product.class)
    private List<Product> products = new ArrayList<Product>();

    @Join
    @Key(types=String.class)
    @Value(types=Product.class)
    private Map<String,Product> productsByName = new HashMap<String,Product>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Product> getProductsByName() {
        return productsByName;
    }

    public void setProductsByName(Map<String, Product> productsByName) {
        this.productsByName = productsByName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
