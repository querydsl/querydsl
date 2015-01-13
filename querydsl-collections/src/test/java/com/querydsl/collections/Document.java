package com.querydsl.collections;

import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public class Document {

    private Long id;

    private List<String> meshThesaurusTerms = new ArrayList<String>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getMeshThesaurusTerms() {
        return meshThesaurusTerms;
    }

    public void setMeshThesaurusTerms(List<String> meshThesaurusTerms) {
        this.meshThesaurusTerms = meshThesaurusTerms;
    }

}