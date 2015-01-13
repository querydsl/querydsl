package com.querydsl.jpa.domain14;

public abstract class SiCZuCapiRechtMapping {

    private MappingID id;

    public void setId(MappingID id) {
        this.id = id;
    }

    public int getSystemID() {
        return id.getSystemID();
    }

    public int getCapiPID() {
        return id.getCapiID();
    }
}