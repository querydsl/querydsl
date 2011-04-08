package com.mysema.query.codegen;

import java.util.List;
import java.util.Map;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class ExampleEntity extends ExampleSupertype {

    private String name;

    private ExampleEntity mate;

    private List<ExampleEntity> mates;

    private Map<String, ExampleEntity> matesByName;

    private ExampleEmbeddable embeddable;

    @QueryEmbedded
    private ExampleEmbedded embedded;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExampleEntity getMate() {
        return mate;
    }

    public void setMate(ExampleEntity mate) {
        this.mate = mate;
    }

    public ExampleEmbeddable getEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(ExampleEmbeddable embeddable) {
        this.embeddable = embeddable;
    }

    public List<ExampleEntity> getMates() {
        return mates;
    }

    public void setMates(List<ExampleEntity> mates) {
        this.mates = mates;
    }

    public Map<String, ExampleEntity> getMatesByName() {
        return matesByName;
    }

    public void setMatesByName(Map<String, ExampleEntity> matesByName) {
        this.matesByName = matesByName;
    }

    public ExampleEmbedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(ExampleEmbedded embedded) {
        this.embedded = embedded;
    }



}
