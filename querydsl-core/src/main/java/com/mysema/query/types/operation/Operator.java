package com.mysema.query.types.operation;

import java.util.List;

public interface Operator<RT> {

    List<Class<?>> getTypes();

}