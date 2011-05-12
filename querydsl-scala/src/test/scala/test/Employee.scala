package test;

import scala.reflect.BeanProperty;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import com.mysema.query.sql.Column;

/**
 * Employee is a Querydsl bean type
 */
class Employee {

  @Column("FIRSTNAME")
  @Size(max=50)
  @BeanProperty var firstname: String = _;

  @Column("ID")
  @NotNull
  @BeanProperty var id: Integer = _;

  @Column("LASTNAME")
  @Size(max=50)
  @BeanProperty var lastname: String = _;

  @Column("SUPERIOR_ID")
  @BeanProperty var superiorId: Integer = _;

}

