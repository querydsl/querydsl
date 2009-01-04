<#import "/macros.ftl" as cl>
package ${package};

import com.mysema.query.grammar.types.*;
import static com.mysema.query.grammar.types.PathMetadata.*;

/**
 * ${classSimpleName} provides types for use in Query DSL constructs
 *
 */
public class ${pre}${classSimpleName} extends Path.PEntity<${type.name}>{
<@cl.classContent decl=type embeddable=false/>
}
