package com.mysema.query.jpa.domain2;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class UserProp {
    
    @Id
    private long id;

    private String categoryName, categoryDescription;
    
    private double createdBy;
    
    private Date creationDate;
    
    private double modifiedBy;
    
    private Date modificationDate;
    
    private double deletedBy;

    private Date deleteDate;
    
    @OneToMany
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public double getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(double modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public double getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(double deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
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

