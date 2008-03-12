<#assign reserved = ["isnull", "isnotnull", "getType", "getMetadata", "toString", "hashCode", "getClass", "notify", "notifyAll", "wait"]>
package ${package};

import com.mysema.query.grammar.PathMetadata;
import static com.mysema.query.grammar.PathMetadata.*;
import static com.mysema.query.grammar.Types.*;

/**
 * ${classSimpleName} provides types for use in Query DSL constructs
 *
 */
public class ${classSimpleName} {
${include}             
<#list domainTypes as decl>               
    public static final class ${pre}${decl.simpleName} extends PathEntity<${decl.name}>{
	<#list decl.stringFields as field>
    	public final PathString ${field.name} = _string("${field.name}");
    </#list>    
    <#list decl.booleanFields as field>
    	public final PathBoolean ${field.name} = _boolean("${field.name}");
    </#list>
    <#list decl.simpleFields as field>
		public final PathComparable<${field.typeName}> ${field.name} = _comparable("${field.name}",${field.typeName}.class);
    </#list>
    <#list decl.entityMaps as field>
    	public final PathEntityMap<${field.keyTypeName},${field.typeName}> ${field.name} = _entitymap("${field.name}",${field.keyTypeName}.class,${field.typeName}.class);
    	public ${pre}${field.simpleTypeName} ${field.name}(${field.keyTypeName} key) {
    		return new ${pre}${field.simpleTypeName}(forMapAccess(${field.name},key));
    	}
    	public ${pre}${field.simpleTypeName} ${field.name}(Expr<${field.keyTypeName}> key) {
    		return new ${pre}${field.simpleTypeName}(forMapAccess(${field.name},key));
    	}
    </#list>
	<#list decl.simpleMaps as field>
    	public final PathComponentMap<${field.keyTypeName},${field.typeName}> ${field.name} = _simplemap("${field.name}",${field.keyTypeName}.class,${field.typeName}.class);
    	public PathNoEntitySimple<${field.typeName}> ${field.name}(${field.keyTypeName} key) {
    		return new PathNoEntitySimple<${field.typeName}>(${field.typeName}.class,forMapAccess(${field.name},key));
    	}
    	public PathNoEntitySimple<${field.typeName}> ${field.name}(Expr<${field.keyTypeName}> key) {
    		return new PathNoEntitySimple<${field.typeName}>(${field.typeName}.class,forMapAccess(${field.name},key));
    	}
    </#list>     
    <#list decl.entityCollections as field>
    	public final PathEntityCollection<${field.typeName}> ${field.name} = _entitycol("${field.name}",${field.typeName}.class);
    	public ${pre}${field.simpleTypeName} ${field.name}(int index) {
    		return new ${pre}${field.simpleTypeName}(forListAccess(${field.name},index));
    	}
    	public ${pre}${field.simpleTypeName} ${field.name}(Expr<Integer> index) {
    		return new ${pre}${field.simpleTypeName}(forListAccess(${field.name},index));
    	}
    </#list>
	<#list decl.simpleCollections as field>
    	public final PathComponentCollection<${field.typeName}> ${field.name} = _simplecol("${field.name}",${field.typeName}.class);
    	public PathNoEntitySimple<${field.typeName}> ${field.name}(int index) {
    		return new PathNoEntitySimple<${field.typeName}>(${field.typeName}.class,forListAccess(${field.name},index));
    	}
    	public PathNoEntitySimple<${field.typeName}> ${field.name}(Expr<Integer> index) {
    		return new PathNoEntitySimple<${field.typeName}>(${field.typeName}.class,forListAccess(${field.name},index));
    	}
    </#list>    
  	<#list decl.entityFields as field>
        public final PathEntityRenamable<${field.typeName}> ${field.name} = _entity("${field.name}",${field.typeName}.class);
	</#list>     
        public ${pre}${decl.simpleName}(String path) {super(${decl.name}.class,path);}
        public ${pre}${decl.simpleName}(PathMetadata<?> metadata) {super(${decl.name}.class, metadata);}
  	<#list decl.entityFields as field>
  	<#if !reserved?seq_contains(field.name)>
  	    private ${pre}${field.simpleTypeName} _${field.name};
        public ${pre}${field.simpleTypeName} ${field.name}() {
            if (_${field.name} == null) _${field.name} = new ${pre}${field.simpleTypeName}(forProperty(this,"${field.name}"));
            return _${field.name};
        }
    </#if>
	</#list>  
    }
        
</#list>
    
}
