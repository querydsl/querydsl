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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="document2_")
public class Document2 {
    
    @Id
    private long id;
    
    private String documentTitle;
    
    private String documentBody;
    
    private String documentSummary;
    
    private double createdBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    private double modifiedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    
    private double deletedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;
    
    private double documentVersion;

    private double documentStatus;
    
    private double entryId;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDocumentBody() {
        return documentBody;
    }

    public void setDocumentBody(String documentBody) {
        this.documentBody = documentBody;
    }

    public String getDocumentSummary() {
        return documentSummary;
    }

    public void setDocumentSummary(String documentSummary) {
        this.documentSummary = documentSummary;
    }

    public double getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(double createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return new Date(creationDate.getTime());
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = new Date(creationDate.getTime());
    }

    public double getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(double modifiedBy) {
        this.modifiedBy = modifiedBy;
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

    public Date getDeletedDate() {
        return new Date(deletedDate.getTime());
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = new Date(deletedDate.getTime());
    }

    public double getDocumentVersion() {
        return documentVersion;
    }

    public void setDocumentVersion(double documentVersion) {
        this.documentVersion = documentVersion;
    }

    public double getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(double documentStatus) {
        this.documentStatus = documentStatus;
    }

    public double getEntryId() {
        return entryId;
    }

    public void setEntryId(double entryId) {
        this.entryId = entryId;
    }


}
