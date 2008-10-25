<#assign reserved = ["isnull", "isnotnull", "getType", "getMetadata", "toString", "hashCode", "getClass", "notify", "notifyAll", "wait"]>
package ${package};

import com.mysema.query.grammar.types.*;
import static com.mysema.query.grammar.types.PathMetadata.*;

/**
 * ${classSimpleName} provides types for use in Query DSL constructs
 *
 */
public class ${classSimpleName} {
${include}             
<#list domainTypes as decl>               
    public static final class ${pre}${decl.simpleName} extends Path.Entity<${decl.name}>{
	<#list decl.stringFields as field>
    	public final Path.String ${field.name} = _string("${field.name}");
    </#list>    
    <#list decl.booleanFields as field>
    	public final Path.Boolean ${field.name} = _boolean("${field.name}");
    </#list>
<#-- 
	simple fields
--> 
    <#list decl.simpleFields as field>
		public final Path.Comparable<${field.typeName}> ${field.name} = _comparable("${field.name}",${field.typeName}.class);
    	</#list>
	<#list decl.simpleMaps as field>
    	public final Path.ComponentMap<${field.keyTypeName},${field.typeName}> ${field.name} = _simplemap("${field.name}",${field.keyTypeName}.class,${field.typeName}.class);
    	public Path.Simple<${field.typeName}> ${field.name}(${field.keyTypeName} key) {
    		return new Path.Simple<${field.typeName}>(${field.typeName}.class,forMapAccess(${field.name},key));
    	}
    	public Path.Simple<${field.typeName}> ${field.name}(Expr<${field.keyTypeName}> key) {
    		return new Path.Simple<${field.typeName}>(${field.typeName}.class,forMapAccess(${field.name},key));
    	}
   	</#list>  
	<#list decl.simpleCollections as field>
    	public final Path.ComponentCollection<${field.typeName}> ${field.name} = _simplecol("${field.name}",${field.typeName}.class);
    </#list>  
	<#list decl.simpleLists as field>
    	public final Path.ComponentList<${field.typeName}> ${field.name} = _simplelist("${field.name}",${field.typeName}.class);
    	public Path.Simple<${field.typeName}> ${field.name}(int index) {
    		return new Path.Simple<${field.typeName}>(${field.typeName}.class,forListAccess(${field.name},index));
    	}
    	public Path.Simple<${field.typeName}> ${field.name}(Expr<Integer> index) {
    		return new Path.Simple<${field.typeName}>(${field.typeName}.class,forListAccess(${field.name},index));
    	}
    </#list>             
<#-- 
	entity fields
-->           
    <#list decl.entityMaps as field>
    	public final Path.EntityMap<${field.keyTypeName},${field.typeName}> ${field.name} = _entitymap("${field.name}",${field.keyTypeName}.class,${field.typeName}.class);
    	public ${pre}${field.simpleTypeName} ${field.name}(${field.keyTypeName} key) {
    		return new ${pre}${field.simpleTypeName}(forMapAccess(${field.name},key));
    	}
    	public ${pre}${field.simpleTypeName} ${field.name}(Expr<${field.keyTypeName}> key) {
    		return new ${pre}${field.simpleTypeName}(forMapAccess(${field.name},key));
    	}
    </#list> 
    <#list decl.entityCollections as field>
    	public final Path.EntityCollection<${field.typeName}> ${field.name} = _entitycol("${field.name}",${field.typeName}.class);
    </#list>
    <#list decl.entityLists as field>
    	public final Path.EntityList<${field.typeName}> ${field.name} = _entitylist("${field.name}",${field.typeName}.class);
    	public ${pre}${field.simpleTypeName} ${field.name}(int index) {
    		return new ${pre}${field.simpleTypeName}(forListAccess(${field.name},index));
    	}
    	public ${pre}${field.simpleTypeName} ${field.name}(Expr<Integer> index) {
    		return new ${pre}${field.simpleTypeName}(forListAccess(${field.name},index));
    	}
    </#list>     
  	<#list decl.entityFields as field>
		public ${pre}${field.simpleTypeName} ${field.name};
		<#if !reserved?seq_contains(field.name)>
        public ${pre}${field.simpleTypeName} _${field.name}() {
            if (${field.name} == null) ${field.name} = new ${pre}${field.simpleTypeName}(forProperty(this,"${field.name}"));
            return ${field.name};
        }
    	</#if>
	</#list>
<#-- 
	constructors
-->  	     
        public ${pre}${decl.simpleName}(java.lang.String path) {
        	super(${decl.name}.class, path);
        <#list decl.entityFields as field>
        	<#if !reserved?seq_contains(field.name)>
        	_${field.name}();
        	</#if>	
        </#list>     
        }
        public ${pre}${decl.simpleName}(PathMetadata<?> metadata) {
        	super(${decl.name}.class, metadata);
        }
    }
        
</#list>
    
}
