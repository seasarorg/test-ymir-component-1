<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

<#list classDesc.annotationDescs as annotationDesc>${annotationDesc.string}
</#list>public class ${classDesc.shortName}Base
{
<#list classDesc.propertyDescs as propertyDesc>
    protected ${propertyDesc.typeDesc.name} ${fieldPrefix}${propertyDesc.name}${fieldSuffix};

</#list>

    public ${classDesc.shortName}Base()
    {
    }

    public ${classDesc.shortName}Base(<#list classDesc.propertyDescs as propertyDesc>${propertyDesc.typeDesc.name} ${propertyDesc.name}<#if propertyDesc_has_next>, </#if></#list>)
    {
<#list classDesc.propertyDescs as propertyDesc>
        ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix} = ${propertyDesc.name};
</#list>
    }
<#list classDesc.propertyDescs as propertyDesc>
<#if propertyDesc.readable>

    public ${propertyDesc.typeDesc.name} <#if propertyDesc.typeDesc.name == "boolean">is<#else>get</#if>${propertyDesc.name?cap_first}()
    {
        return ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix};
    }
</#if>
<#if propertyDesc.writable>

    public void set${propertyDesc.name?cap_first}(${propertyDesc.typeDesc.name} ${propertyDesc.name})
    {
        ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix} = ${propertyDesc.name};
    }
</#if>
</#list>
}
