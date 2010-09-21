package test;

import com.mysema.query.sql._;
import com.mysema.query.types.path._;

import com.mysema.query.sql.Table;

import java.util.Arrays;

/**
 * Employee is a Querydsl query type
 */
@Table("EMPLOYEE")
class QEmployee(path: String) extends RelationalPathBase[Employee](classOf[Employee], path) {
    val firstname: StringPath = createString("FIRSTNAME");

    val id: NumberPath[Integer] = createNumber("ID", classOf[Integer]);

    val lastname: StringPath = createString("LASTNAME");

    val superiorId: NumberPath[Integer] = createNumber("SUPERIOR_ID", classOf[Integer]);

    val sysIdx55: PrimaryKey[Employee] = createPrimaryKey(id);

    val superiorFk: ForeignKey[Employee] = createForeignKey(superiorId, "ID");

    val _superiorFk: ForeignKey[Employee] = createInvForeignKey(id, "SUPERIOR_ID");

}

