package com.mysema.query.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.junit.Test;

public class PropertiesTest {
    
    @Entity
    public abstract class AbstractEntity {
        
    }
    
    @Entity
    @Table(name = "Customer")
    public class Customer extends AbstractEntity {

        private String name;
        private List<Pizza> pizzas = new ArrayList<Pizza>(0);

        @Column
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @OneToMany(mappedBy = "customer")
        public List<Pizza> getPizzas() {
            return pizzas;
        }

        public void setPizzas(List<Pizza> pizzas) {
            this.pizzas = pizzas;
        }
    }
    
    @Entity
    @Table(name = "Pizza")
    public class Pizza extends AbstractEntity {

        private Date orderTime;
        private Customer customer;
        private List<Topping> toppings = new ArrayList<Topping>(0);

        @Column @Temporal(TemporalType.TIMESTAMP)
        public Date getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(Date orderTime) {
            this.orderTime = orderTime;
        }

        @ManyToOne
        @JoinColumn(name = "customerId")
        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }

        @OneToMany(mappedBy = "pizza")
        public List<Topping> getToppings() {
            return toppings;
        }

        public void setToppings(List<Topping> toppings) {
            this.toppings = toppings;
        }
    }

    @Entity
    public class Topping {
        
    }
    
    @Test
    public void Customer() {
        assertNotNull(QPropertiesTest_Customer.customer.name);
        assertNotNull(QPropertiesTest_Customer.customer.pizzas);
    }
    
    @Test
    public void Pizza() {
        assertNotNull(QPropertiesTest_Pizza.pizza.orderTime);
        assertNotNull(QPropertiesTest_Pizza.pizza.customer);
        assertNotNull(QPropertiesTest_Pizza.pizza.toppings);
    }
    
}
