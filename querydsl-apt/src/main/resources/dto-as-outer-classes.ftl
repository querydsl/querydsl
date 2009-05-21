<#import "/macros.ftl" as cl>
package ${package};

import com.mysema.query.types.expr.*;

/**
 * ${pre}${classSimpleName} is a Querydsl DTO type
 *
 */
public class ${pre}${classSimpleName} extends EConstructor<${type.name}>{
    <#list type.constructors as co>    
        public ${pre}${type.simpleName}(<#list co.parameters as pa>Expr<${pa.typeName}> ${pa.name}<#if pa_has_next>,</#if></#list>){
            super(${type.name}.class<#list co.parameters as pa>,${pa.name}</#list>);
        }
    </#list>   
}
