package com.querydsl.jpa.domain5;

import java.util.HashSet;
import java.util.Set;


public class Person<T extends Contact, H extends HistoryEntity> {

    private Long id;
    
    private Set<T> contacts = new HashSet<T>();

    private Set<H> history = new HashSet<H>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<T> getContacts() {
        return contacts;
    }

    public void setContacts(Set<T> contacts) {
        this.contacts = contacts;
    }

    public Set<H> getHistory() {
        return history;
    }

    public void setHistory(Set<H> history) {
        this.history = history;
    }

    
}