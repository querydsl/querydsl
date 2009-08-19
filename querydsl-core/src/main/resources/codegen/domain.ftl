<#import "/codegen/macros.ftl" as cl>
package ${package};

import com.mysema.query.util.*;
import com.mysema.query.types.path.*;

/**
 * ${pre}${classSimpleName} is a Querydsl query type for ${classSimpleName}
 *
 */
public class ${pre}${classSimpleName} extends PEntity<${type.simpleName}>{

<@cl.classContent decl=type embeddable=false/>
}
