<#import "/macros.ftl" as cl>
package ${package};

import com.mysema.query.grammar.types.*;

/**
 * ${pre}${classSimpleName} is a Querydsl embeddable type
 *
 */
public class ${pre}${classSimpleName} extends Path.PEntity<${type.name}>{
<@cl.classContent decl=type embeddable=true/>
}
