package com.mysema.query;

public class DeleteUseLiteralsBase extends DeleteBase {

    public DeleteUseLiteralsBase() {
        configuration.setUseLiterals(true);
    }

}
