<#import "/codegen/macros.ftl" as cl>
package ${package};

import com.mysema.query.types.path.*;

/**
 * ${pre}${classSimpleName} is a Querydsl embeddable type
 *
 */
@SuppressWarnings("all")
public class ${pre}${classSimpleName} extends PEntity<${type.localName}>{

<@cl.classContent decl=type embeddable=true/>
}
