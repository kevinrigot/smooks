/**
 * This class was generated by Smooks EJC (http://www.smooks.org).
 */
package ${class.packageName};

<#list class.imports as importClass>
import ${importClass.name};
</#list>

<#if class.documentation??>
/** ${class.documentation} */
</#if>
<#list class.annotationTypes as annotationType>
@${annotationType.type.simpleName}
</#list>
public class ${class.className}${class.implementsDecl}${class.extendsDecl} {

    <#if class.serializable>
    private static final long serialVersionUID = 1L;
    </#if>

    <#list class.properties as property>
    private ${property}${property.defaultValueToString};
    </#list>
    <#list class.constructors as constructor>

    public ${class.className}${constructor.paramSignature} {
        ${constructor.body}
    }
    </#list>
    <#list class.methods as method>
    	<#if method.documentation??>
    /** ${method.documentation} */
    	</#if>
    public ${method.returnType} ${method.methodName}${method.paramSignature} {
        ${method.body}
    }
    </#list>
}