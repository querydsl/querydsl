<#macro classContent decl embeddable>
  <#assign reserved = ["isnull", "isnotnull", "getType", "getMetadata", "toString", "hashCode", "getClass", "notify", "notifyAll", "wait"]>
  <#list decl.stringFields as field>
    	public final Path.PString ${field.name} = _string("${field.name}");
    </#list>    
    <#list decl.booleanFields as field>
    	public final Path.PBoolean ${field.name} = _boolean("${field.name}");
    </#list>
    
  <#-- simple fields --> 
    <#list decl.simpleFields as field>
		public final Path.PSimple<${field.typeName}> ${field.name} = _simple("${field.name}",${field.typeName}.class);
    </#list>
    <#list decl.comparableFields as field>
		public final Path.PComparable<${field.typeName}> ${field.name} = _comparable("${field.name}",${field.typeName}.class);
    </#list>
	<#list decl.simpleMaps as field>
    	public final Path.PComponentMap<${field.keyTypeName},${field.typeName}> ${field.name} = _simplemap("${field.name}",${field.keyTypeName}.class,${field.typeName}.class);
    	public Path.PSimple<${field.typeName}> ${field.name}(${field.keyTypeName} key) {
    		return new Path.PSimple<${field.typeName}>(${field.typeName}.class,forMapAccess(${field.name},key));
    	}
    	public Path.PSimple<${field.typeName}> ${field.name}(Expr<${field.keyTypeName}> key) {
    		return new Path.PSimple<${field.typeName}>(${field.typeName}.class,forMapAccess(${field.name},key));
    	}
   	</#list>  
	<#list decl.simpleCollections as field>
    	public final Path.PComponentCollection<${field.typeName}> ${field.name} = _simplecol("${field.name}",${field.typeName}.class);
    </#list>  
	<#list decl.simpleLists as field>
    	public final Path.PComponentList<${field.typeName}> ${field.name} = _simplelist("${field.name}",${field.typeName}.class);
    	public Path.PSimple<${field.typeName}> ${field.name}(int index) {
    		return new Path.PSimple<${field.typeName}>(${field.typeName}.class,forListAccess(${field.name},index));
    	}
    	public Path.PSimple<${field.typeName}> ${field.name}(Expr<Integer> index) {
    		return new Path.PSimple<${field.typeName}>(${field.typeName}.class,forListAccess(${field.name},index));
    	}
    </#list>             
    
  <#-- entity fields -->           
    <#list decl.entityMaps as field>
    	public final Path.PEntityMap<${field.keyTypeName},${field.typeName}> ${field.name} = _entitymap("${field.name}",${field.keyTypeName}.class,${field.typeName}.class);
    	public ${pre}${field.simpleTypeName} ${field.name}(${field.keyTypeName} key) {
    		return new ${pre}${field.simpleTypeName}(forMapAccess(${field.name},key));
    	}
    	public ${pre}${field.simpleTypeName} ${field.name}(Expr<${field.keyTypeName}> key) {
    		return new ${pre}${field.simpleTypeName}(forMapAccess(${field.name},key));
    	}
    </#list> 
    <#list decl.entityCollections as field>
    	public final Path.PEntityCollection<${field.typeName}> ${field.name} = _entitycol("${field.name}",${field.typeName}.class);
    </#list>
    <#list decl.entityLists as field>
    	public final Path.PEntityList<${field.typeName}> ${field.name} = _entitylist("${field.name}",${field.typeName}.class);
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
	
  <#-- constructors -->  	     
    <#if !embeddable>
        public ${pre}${decl.simpleName}(java.lang.String path) {
        	super(${decl.name}.class, path);
        <#list decl.entityFields as field>
        	<#if !reserved?seq_contains(field.name)>
        	_${field.name}();
        	</#if>	
        </#list>     
        }
    </#if>     
        public ${pre}${decl.simpleName}(PathMetadata<?> metadata) {
        	super(${decl.name}.class, metadata);
        }
</#macro>        