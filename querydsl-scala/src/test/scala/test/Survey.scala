package test;

import scala.reflect.BeanProperty;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import com.mysema.query.sql._;

/**
 * Survey is a Querydsl bean type
 */
@Schema("PUBLIC")
@Table("SURVEY")
class Survey {

 var id: Integer = _;

 var name: String = _;

}

