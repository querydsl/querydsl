package com.mysema.query;

public class InsertUseLiteralsBase extends InsertBase {

    public InsertUseLiteralsBase() {
        configuration.setUseLiterals(true);
    }

}
