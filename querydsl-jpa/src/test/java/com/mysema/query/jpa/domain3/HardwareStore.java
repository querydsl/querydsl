package com.mysema.query.jpa.domain3;

public class HardwareStore extends Store{
    
    private static final long serialVersionUID = 2725944536560445206L;
    
    private String storeCode;

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }
    
}
