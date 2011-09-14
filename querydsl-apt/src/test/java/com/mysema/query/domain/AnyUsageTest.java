package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.junit.Test;

public class AnyUsageTest {

    @Entity
    public class DealerGroup implements Serializable {
        private static final long serialVersionUID = 8001287260658920066L;

        @Id
        @GeneratedValue
        public Long id;

        @OneToMany(mappedBy = "dealerGroup")
        public Set<Dealer> dealers;

    }

    @Entity
    public class Dealer implements Serializable {
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
    public class Company {
        
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

}
