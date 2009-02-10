<#assign reserved = ["isnull", "isnotnull", "getType", "getMetadata", "toString", "hashCode", "getClass", "notify", "notifyAll", "wait"]>

<#macro classContent decl embeddable>    
    <#list decl.stringFields as field>
    	<@stringField field=field/>
    </#list>    
    <#list decl.booleanFields as field>
    	<@booleanField field=field/>	
    </#list>    
    <#list decl.simpleFields as field>
		<@simpleField field=field/>
    </#list>
    <#list decl.comparableFields as field>
		<@comparableField field=field/>
    </#list>
    <#list decl.numericFields as field>
		<@numericField field=field/>
    </#list>
	<#list decl.simpleMaps as field>
    	<@simpleMap field=field/>
   	</#list>  
	<#list decl.simpleCollections as field>
    	<@simpleCollection field=field/>
    </#list>  
	<#list decl.simpleLists as field>
    	<@simpleList field=field/>
    </#list>                
    <#list decl.entityMaps as field>
    	<@entityMap field=field/>
    </#list> 
    <#list decl.entityCollections as field>
    	<@entityCollection field=field/>
    </#list>
    <#list decl.entityLists as field>
    	<@entityList field=field/>
    </#list>     
  	<#list decl.entityFields as field>
		<@entityField field=field/>
	</#list>
	
  <#-- constructors -->  	     
    <#if !embeddable>
    public ${pre}${decl.simpleName}(java.lang.String path) {
      	super(${decl.name}.class, "${decl.simpleName}", path);
    <#list decl.entityFields as field>
      	<#if !reserved?seq_contains(field.name)>
      	_${field.name}();
      	</#if>	
    </#list>     
    }
    </#if>     
    public ${pre}${decl.simpleName}(PathMetadata<?> metadata) {
     	super(${decl.name}.class, "${decl.simpleName}", metadata);
    }
</#macro>   

<#macro booleanField field>
	public final Path.PBoolean ${field.name} = _boolean("${field.realName}");
</#macro>

<#macro comparableField field>
	public final Path.PComparable<${field.typeName}> ${field.name} = _comparable("${field.realName}",${field.typeName}.class);
</#macro>

<#macro entityCollection field>
	public final Path.PEntityCollection<${field.typeName}> ${field.name} = _entitycol("${field.realName}",${field.typeName}.class, "${field.simpleTypeName}");
</#macro>

<#macro entityField field>
	public ${pre}${field.simpleTypeName} ${field.name};
	<#if !reserved?seq_contains(field.name)>
    public ${pre}${field.simpleTypeName} _${field.name}() {
         if (${field.name} == null) ${field.name} = new ${pre}${field.simpleTypeName}(PathMetadata.forProperty(this,"${field.realName}"));
         return ${field.name};
    }
    </#if>
</#macro>

<#macro entityList field>
	public final Path.PEntityList<${field.typeName}> ${field.name} = _entitylist("${field.realName}",${field.typeName}.class,"${field.simpleTypeName}");
    public ${pre}${field.simpleTypeName} ${field.name}(int index) {
    	return new ${pre}${field.simpleTypeName}(PathMetadata.forListAccess(${field.name},index));
    }
    public ${pre}${field.simpleTypeName} ${field.name}(Expr<Integer> index) {
    	return new ${pre}${field.simpleTypeName}(PathMetadata.forListAccess(${field.name},index));
    }
</#macro>

<#macro entityMap field>
	public final Path.PEntityMap<${field.keyTypeName},${field.typeName}> ${field.name} = _entitymap("${field.realName}",${field.keyTypeName}.class,${field.typeName}.class,"${field.simpleTypeName}");
    public ${pre}${field.simpleTypeName} ${field.name}(${field.keyTypeName} key) {
    	return new ${pre}${field.simpleTypeName}(PathMetadata.forMapAccess(${field.name},key));
    }
    public ${pre}${field.simpleTypeName} ${field.name}(Expr<${field.keyTypeName}> key) {
    	return new ${pre}${field.simpleTypeName}(PathMetadata.forMapAccess(${field.name},key));
    }
</#macro>
     
<#macro numericField field>
	public final Path.PNumber<${field.typeName}> ${field.name} = _number("${field.realName}",${field.typeName}.class);
</#macro>
         
<#macro simpleField field>
	public final Path.PSimple<${field.typeName}> ${field.name} = _simple("${field.realName}",${field.typeName}.class);
</#macro>
     
<#macro simpleMap field>
	public final Path.PComponentMap<${field.keyTypeName},${field.typeName}> ${field.name} = _simplemap("${field.realName}",${field.keyTypeName}.class,${field.typeName}.class);
    public Path.PSimple<${field.typeName}> ${field.name}(${field.keyTypeName} key) {
    	return new Path.PSimple<${field.typeName}>(${field.typeName}.class,PathMetadata.forMapAccess(${field.name},key));
    }
    public Path.PSimple<${field.typeName}> ${field.name}(Expr<${field.keyTypeName}> key) {
    	return new Path.PSimple<${field.typeName}>(${field.typeName}.class,PathMetadata.forMapAccess(${field.name},key));
    }
</#macro>     

<#macro simpleCollection field>
	public final Path.PComponentCollection<${field.typeName}> ${field.name} = _simplecol("${field.realName}",${field.typeName}.class);
</#macro>

<#macro simpleList field>
	public final Path.PComponentList<${field.typeName}> ${field.name} = _simplelist("${field.realName}",${field.typeName}.class);
    public Path.PSimple<${field.typeName}> ${field.name}(int index) {
    	return new Path.PSimple<${field.typeName}>(${field.typeName}.class,PathMetadata.forListAccess(${field.name},index));
    }
    public Path.PSimple<${field.typeName}> ${field.name}(Expr<Integer> index) {
    	return new Path.PSimple<${field.typeName}>(${field.typeName}.class,PathMetadata.forListAccess(${field.name},index));
    }
</#macro>

<#macro stringField field>
	public final Path.PString ${field.name} = _string("${field.realName}");
</#macro>


