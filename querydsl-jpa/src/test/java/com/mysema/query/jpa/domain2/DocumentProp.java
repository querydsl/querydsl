package com.mysema.query.jpa.domain2;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DocumentProp {

    @Id
    private long id;
    
    private double documentId;
    
    private String propName, propValue, propValueDetails;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getDocumentId() {
        return documentId;
    }

    public void setDocumentId(double documentId) {
        this.documentId = documentId;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getPropValueDetails() {
        return propValueDetails;
    }

    public void setPropValueDetails(String propValueDetails) {
        this.propValueDetails = propValueDetails;
    }

    
    
}
