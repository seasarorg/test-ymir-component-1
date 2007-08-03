<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

<#list classDesc.annotationDescs as annotationDesc>${annotationDesc.string}
</#list>public <#if classDesc.baseClassAbstract>abstract </#if>class ${classDesc.shortName}Base<#if classDesc.superclassName?exists> extends ${classDesc.superclassName}</#if>
{
<#list classDesc.propertyDescs as propertyDesc>
    protected ${propertyDesc.typeDesc.name} ${fieldPrefix}${propertyDesc.name}${fieldSuffix};

</#list>
<#list classDesc.propertyDescs as propertyDesc>
<#if propertyDesc.readable>

    public ${propertyDesc.typeDesc.name} <#if propertyDesc.typeDesc.name == "boolean">is<#else>get</#if>${propertyDesc.name?cap_first}()
    {
        return ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix};
    }
</#if>
<#if propertyDesc.writable>

<#list propertyDesc.annotationDescs as annotationDesc>    ${annotationDesc.string}
</#list>    public void set${propertyDesc.name?cap_first}(${propertyDesc.typeDesc.name} ${propertyDesc.name})
    {
        ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix} = ${propertyDesc.name};
    }
</#if>
</#list>
<#list classDesc.methodDescs as methodDesc>

<#list methodDesc.annotationDescs as annotationDesc>    ${annotationDesc.string}
</#list>    public ${methodDesc.returnTypeDesc.name} ${methodDesc.name}(<#list methodDesc.parameterDescs as parameterDesc>${parameterDesc.typeDesc.name} ${parameterDesc.name}<#if parameterDesc_has_next>, </#if></#list>)<#if !methodDesc.throwsDesc.empty>
        throws </#if><#list methodDesc.throwsDesc.throwableClassNames as throwableClassName>${throwableClassName}<#if throwableClassName_has_next>, </#if></#list>
    {
<#if methodDesc.evaluatedBody?exists>        ${methodDesc.evaluatedBody}<#elseif methodDesc.returnTypeDesc.name != "void">        return ${methodDesc.returnTypeDesc.defaultValue};</#if>
    }
</#list>
}
