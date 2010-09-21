package test;

import com.mysema.query.sql._;
import com.mysema.query.types.path._;

import com.mysema.query.sql.Table;

import java.util.Arrays;

/**
 * Survey is a Querydsl query type
 */
@Table("SURVEY")
class QSurvey(path: String) extends RelationalPathBase[Survey](classOf[Survey], path) {
    val id: NumberPath[Integer] = createNumber("ID", classOf[Integer]);

    val name: StringPath = createString("NAME");

    val sysIdx54: PrimaryKey[Survey] = createPrimaryKey(id, name);

}

