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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="category_")
public class Category {

    @Id
    private long id;
    
    private String categoryName;
    
    private String categoryDescription;
    
    private double createdBy;
    
    private double modifiedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    
    private double deletedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;
    
    @OneToMany
    @JoinTable(joinColumns = @JoinColumn(name = "parentId"), inverseJoinColumns = @JoinColumn(name = "childId"))
    private Set<Category> childCategories;
    
    @OneToMany
    private Set<CategoryProp> properties;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public double getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(double createdBy) {
        this.createdBy = createdBy;
    }

    public double getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(double modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getCreationDate() {
        return new Date(creationDate.getTime());
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = new Date(creationDate.getTime());
    }

    public Date getModificationDate() {
        return new Date(modificationDate.getTime());
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = new Date(modificationDate.getTime());
    }

    public double getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(double deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Date getDeleteDate() {
        return new Date(deleteDate.getTime());
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = new Date(deleteDate.getTime());
    }

    public Set<Category> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(Set<Category> childCategories) {
        this.childCategories = childCategories;
    }

    public Set<CategoryProp> getProperties() {
        return properties;
    }

    public void setProperties(Set<CategoryProp> properties) {
        this.properties = properties;
    }
}
