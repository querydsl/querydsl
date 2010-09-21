package test;

import javax.validation.constraints.NotNull;
import com.mysema.query.sql.Column;
import com.mysema.query.sql.Table;
import javax.validation.constraints.Size;
import scala.reflect.BeanProperty;

/**
 * Survey is a Querydsl bean type
 */
@Table("SURVEY")
class Survey {

    @Column("ID")
    @NotNull
    @BeanProperty
    var id: Integer = _;

    @Column("NAME")
    @NotNull
    @Size(max=30)
    @BeanProperty
    var name: String = _;

}

