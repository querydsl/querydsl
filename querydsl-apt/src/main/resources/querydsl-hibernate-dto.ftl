package ${package};

import static com.mysema.query.grammar.HqlGrammar.*;
import static com.mysema.query.grammar.Types.*;

/**
 * ${classSimpleName} provides types for use in Query DSL constructs
 *
 */
public class ${classSimpleName} {

<#list dtoTypes as decl>
    public static final class ${pre}${decl.simpleName} extends Constructor<${decl.name}>{
    <#list decl.constructors as co>    
        public ${pre}${decl.simpleName}(<#list co.parameters as pa>Expr<${pa.typeName}> ${pa.name}<#if pa_has_next>,</#if></#list>){
            super(${decl.name}.class<#list co.parameters as pa>,${pa.name}</#list>);
        }
    </#list>            
    }
    
</#list> 
}
