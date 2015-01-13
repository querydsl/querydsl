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
package com.querydsl.apt.domain;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.junit.Test;

import com.querydsl.apt.domain.QAnyUsageTest_Dealer;
import com.querydsl.apt.domain.QAnyUsageTest_DealerGroup;
import com.querydsl.core.types.expr.BooleanExpression;

public class AnyUsageTest {

    @Entity
    public static class DealerGroup implements Serializable {
        private static final long serialVersionUID = 8001287260658920066L;

        @Id
        @GeneratedValue
        public Long id;

        @OneToMany(mappedBy = "dealerGroup")
        public Set<Dealer> dealers;

    }

    @Entity
    public static class Dealer implements Serializable {
        private static final long serialVersionUID = -6832045219902674887L;

        @Id
        @GeneratedValue
        public Long id;

        @ManyToOne
        public DealerGroup dealerGroup;
        
        @ManyToOne
        public Company company;

    }
    
    @Entity
    public static class Company implements Serializable {        
        private static final long serialVersionUID = -5369301332567282659L;
        
        @Id
        @GeneratedValue
        public Long id;
        
    }

    @Test
    public void test() {
        QAnyUsageTest_Dealer dealer = QAnyUsageTest_DealerGroup.dealerGroup.dealers.any();
        assertNotNull(dealer);
        assertNotNull(dealer.company);
    }
    
    @Test
    public void WithQDealer() {
        List<Company> companies = new LinkedList<Company>();
        companies.add( new Company() );
        QAnyUsageTest_Dealer qDealer = QAnyUsageTest_Dealer.dealer;
        BooleanExpression expression = qDealer.company.in( companies );
        assertNotNull(expression);
    }

    @Test
    public void WithQDealerGroup() {
        List<Company> companies = new LinkedList<Company>();
        companies.add( new Company() );
        QAnyUsageTest_Dealer qDealer = QAnyUsageTest_DealerGroup.dealerGroup.dealers.any();                   
        BooleanExpression expression = qDealer.company.in( companies );
        assertNotNull(expression);
    }

}
