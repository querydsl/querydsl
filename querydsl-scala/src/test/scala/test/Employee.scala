package test;

import javax.validation.constraints.NotNull;
import com.mysema.query.sql.Column;
import com.mysema.query.sql.Table;
import javax.validation.constraints.Size;
import scala.reflect.BeanProperty;

/**
 * Employee is a Querydsl bean type
 */
@Table("EMPLOYEE")
class Employee {

    @Column("FIRSTNAME")
    @Size(max=50)
    @BeanProperty
    var firstname: String = _;

    @Column("ID")
    @NotNull
    @BeanProperty
    var id: Integer = _;

    @Column("LASTNAME")
    @Size(max=50)
    @BeanProperty
    var lastname: String = _;

    @Column("SUPERIOR_ID")
    @BeanProperty
    var superiorId: Integer = _;

}

