package test;
import java.math.BigDecimal;

import com.mysema.query.annotations.QueryEntity;


@QueryEntity
public class Test {

    private String property;

    private int intProperty;

    private BigDecimal bigDecimal;
}
