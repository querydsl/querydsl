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
package com.querydsl.jpa.domain2;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="user2_")
public class User2 {

    @Id
    private long id;
    
    private String userName, userPassword, userFirstName, userEmail, userLastName;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    private double createdBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    
    private double modifiedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;
    
    private double deletedBy;
    
    @OneToMany
    private Set<UserProp> properties;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public Date getCreationDate() {
        return new Date(creationDate.getTime());
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = new Date(creationDate.getTime());
    }

    public double getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(double createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModificationDate() {
        return new Date(modificationDate.getTime());
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = new Date(modificationDate.getTime());
    }

    public double getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(double modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getDeleteDate() {
        return new Date(deleteDate.getTime());
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = new Date(deleteDate.getTime());
    }

    public double getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(double deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Set<UserProp> getProperties() {
        return properties;
    }

    public void setProperties(Set<UserProp> properties) {
        this.properties = properties;
    }

    
}
