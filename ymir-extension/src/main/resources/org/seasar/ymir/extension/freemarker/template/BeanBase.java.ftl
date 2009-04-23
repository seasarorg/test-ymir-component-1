${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>
<#if !importDesc.empty>

${importDesc.asString}</#if>
<#list classDesc.annotationDescs as annotationDesc>${annotationDesc.asShortString}
</#list>public class ${classDesc.shortName}Base {
<#list classDesc.propertyDescs as propertyDesc>
    protected ${propertyDesc.typeDesc.shortName} ${fieldPrefix}${propertyDesc.name}${fieldSuffix};

</#list>

    public ${classDesc.shortName}Base() {
    }

    public ${classDesc.shortName}Base(<#list classDesc.propertyDescsOrderByName as propertyDesc>${propertyDesc.typeDesc.shortName} ${propertyDesc.name}<#if propertyDesc_has_next>, </#if></#list>) {
<#list classDesc.propertyDescsOrderByName as propertyDesc>
        ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix} = ${propertyDesc.name};
</#list>
    }
<#list classDesc.propertyDescs as propertyDesc>
<#if propertyDesc.readable>

<#list propertyDesc.annotationDescsOnGetter as annotationDesc>    ${annotationDesc.asShortString}
</#list>    public ${propertyDesc.typeDesc.shortName} <#if propertyDesc.typeDesc.shortName == "boolean">is<#else>get</#if>${propertyDesc.name?cap_first}() {
        return ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix};
    }
</#if>
<#if propertyDesc.writable>

<#list propertyDesc.annotationDescsOnSetter as annotationDesc>    ${annotationDesc.asShortString}
</#list>    public void set${propertyDesc.name?cap_first}(${propertyDesc.typeDesc.shortName} ${propertyDesc.name}) {
        ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix} = ${propertyDesc.name};
    }
</#if>
</#list>
}
