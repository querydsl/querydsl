package com.mysema.testutil;

import org.junit.runners.model.Statement;

public class EmptyStatement extends Statement{

    public static final Statement DEFAULT = new EmptyStatement();
    
    private EmptyStatement(){}

    @Override
    public void evaluate() throws Throwable {
        
    }

}
