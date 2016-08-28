package ${javabeanPackageName};
/**
 * ${genDate} by freemarker template
 */
public class ${className} {

	<#list fieldList as field>
    private ${field.getValue()} ${field.getKey()};
    
	</#list>
    <#list fieldList as field>
    public void set${field.getKey()?cap_first}(${field.getValue()} ${field.getKey()}){
        this.${field.getKey()} = ${field.getKey()};
    }
    public ${field.getValue()} get${field.getKey()?cap_first}(){
        return this.${field.getKey()};
    }
    
	</#list>
}