<#import "/codegen/macros.ftl" as cl>
package ${package};

import com.mysema.query.types.path.*;

/**
 * ${pre}${classSimpleName} is a Querydsl embeddable type
 *
 */
public class ${pre}${classSimpleName} extends PEntity<${type.name}>{
<@cl.classContent decl=type embeddable=true/>
}
