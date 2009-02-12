${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

public class ${classDesc.shortName}Base {
<#list classDesc.propertyDescs as propertyDesc>
    protected ${propertyDesc.typeDesc.name} ${fieldPrefix}${propertyDesc.name}${fieldSuffix};

</#list>

    public ${classDesc.shortName}Base() {
    }

    public ${classDesc.shortName}Base(<#list classDesc.propertyDescsOrderByName as propertyDesc>${propertyDesc.typeDesc.name} ${propertyDesc.name}<#if propertyDesc_has_next>, </#if></#list>) {
<#list classDesc.propertyDescsOrderByName as propertyDesc>
        ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix} = ${propertyDesc.name};
</#list>
    }
<#list classDesc.propertyDescs as propertyDesc>
<#if propertyDesc.readable>

<#list propertyDesc.annotationDescsForGetter as annotationDesc>    ${annotationDesc.string}
</#list>    public ${propertyDesc.typeDesc.name} <#if propertyDesc.typeDesc.name == "boolean">is<#else>get</#if>${propertyDesc.name?cap_first}() {
        return ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix};
    }
</#if>
<#if propertyDesc.writable>

<#list propertyDesc.annotationDescsForSetter as annotationDesc>    ${annotationDesc.string}
</#list>    public void set${propertyDesc.name?cap_first}(${propertyDesc.typeDesc.name} ${propertyDesc.name}) {
        ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix} = ${propertyDesc.name};
    }
</#if>
</#list>
}
