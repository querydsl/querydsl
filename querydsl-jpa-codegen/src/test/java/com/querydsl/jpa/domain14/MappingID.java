package com.querydsl.jpa.domain14;

import java.io.Serializable;

public class MappingID implements Serializable {

    private static final long serialVersionUID = -4623004134095871109L;
    private short systemID;

    private int capiID;

    public MappingID() {
        // Default-Konstruktor wird vom Hinernate Criteria-API verwendet
    }

    public MappingID(short systemID, int capiID) {
        this.capiID = capiID;
        this.systemID = systemID;
    }

    public short getSystemID() {
        return systemID;
    }

    public int getCapiID() {
        return capiID;
    }
}