package com.mysema.query.jpa.domain;

import com.mysema.query.annotations.QueryProjection;

public class CatSummary {

    private long breeders;

    private boolean hasEyeColoredCat;

    @QueryProjection
    public CatSummary(long breeders, boolean hasEyeColoredCat) {
        super();
        this.breeders = breeders;
        this.hasEyeColoredCat = hasEyeColoredCat;
    }

    public long getBreeders() {
        return breeders;
    }

    public void setBreeders(long breeders) {
        this.breeders = breeders;
    }

    public boolean isHasEyeColoredCat() {
        return hasEyeColoredCat;
    }

    public void setHasEyeColoredCat(boolean hasEyeColoredCat) {
        this.hasEyeColoredCat = hasEyeColoredCat;
    }

}
