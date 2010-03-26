/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.search;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Table(name = "user_")
@Indexed
@Analyzer(impl = org.apache.lucene.analysis.standard.StandardAnalyzer.class)
public class User implements Serializable {

    private static final long serialVersionUID = 5955148455600241741L;

    @Id
    @GeneratedValue
    @DocumentId
    private Long id;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    private String firstName;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    private String lastName;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    private String middleName;

    @NaturalId
    @Field(name="email", index = Index.UN_TOKENIZED, store = Store.YES)
    private String emailAddress;

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "applicant")
    @ContainedIn
    private Set<Resume> resumes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Set<Resume> getResumes() {
        return resumes;
    }

    public void setResumes(Set<Resume> resumes) {
        this.resumes = resumes;
    }

}
