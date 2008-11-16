<#import "/macros.ftl" as cl>
package ${package};

import com.mysema.query.grammar.types.*;
import static com.mysema.query.grammar.types.PathMetadata.*;

/**
 * ${classSimpleName} provides types for use in Query DSL constructs
 *
 */
public class ${classSimpleName} {
${include}             
<#list domainTypes as type>               
    public static final class ${pre}${type.simpleName} extends Path.Entity<${type.name}>{
		<@cl.classContent decl=type/>		
    }
        
</#list>
    
}
