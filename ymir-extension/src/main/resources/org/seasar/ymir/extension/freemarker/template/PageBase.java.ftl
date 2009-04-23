${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>
<#if !importDesc.empty>

${importDesc.asString}</#if>
<#list classDesc.annotationDescs as annotationDesc>
${annotationDesc.asShortString}
</#list>
public <#if classDesc.abstract>abstract </#if>class ${classDesc.shortName}Base<#if classDesc.superclassShortName?exists && classDesc.superclassShortName != "Object"> extends ${classDesc.superclassShortName}</#if><#list classDesc.interfaceTypeDescs as interfaceTypeDesc><#if interfaceTypeDesc_index == 0>
    implements </#if>${interfaceTypeDesc.shortName}<#if interfaceTypeDesc_has_next>, </#if></#list> {
    public static final String PACKAGE = "${classDesc.packageName}";

    public static final String NAME = "${classDesc.nameBase?uncap_first}";
<#if classDesc.pathOfClass??>

    public static final String PATH = "${classDesc.pathOfClass}";
</#if>
<#if classDesc.getAttribute("action")??>
<#list classDesc.getAttribute("action") as methodDesc>

    public static final String A${methodDesc.name} = "${methodDesc.name}";
</#list>
</#if>
<#list classDesc.propertyDescs as propertyDesc>

    public static final String P_${propertyDesc.name} = "${propertyDesc.name}";
</#list>
<#list classDesc.propertyDescs as propertyDesc>
<#if !propertyDesc.hasMetaOnSetter("formProperty")>

<#list propertyDesc.annotationDescs as annotationDesc>
    ${annotationDesc.asShortString}
</#list>
    protected ${propertyDesc.typeDesc.shortName} ${fieldPrefix}${propertyDesc.name}${fieldSuffix}<#if propertyDesc.initialValue??> = ${propertyDesc.initialValue}</#if>;
</#if>
</#list>
<#list classDesc.propertyDescs as propertyDesc>
<#if propertyDesc.readable>

<#list propertyDesc.annotationDescsOnGetter as annotationDesc>
    ${annotationDesc.asShortString}
</#list>
    public ${propertyDesc.typeDesc.shortName} <#if propertyDesc.typeDesc.shortName == "boolean">is<#else>get</#if>${propertyDesc.name?cap_first}() {
        return <#if propertyDesc.hasMetaOnGetter("formProperty")>${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.getMetaFirstValueOnGetter("formProperty")}${fieldSuffix}.<#if propertyDesc.typeDesc.shortName == "boolean">is<#else>get</#if>${propertyDesc.name?cap_first}();<#else>${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix};</#if>
    }
</#if>
<#if propertyDesc.writable>

<#list propertyDesc.annotationDescsOnSetter as annotationDesc>
    ${annotationDesc.asShortString}
</#list>
    public void set${propertyDesc.name?cap_first}(${propertyDesc.typeDesc.shortName} ${propertyDesc.name}) {
        <#if propertyDesc.hasMetaOnSetter("formProperty")>${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.getMetaFirstValueOnSetter("formProperty")}${fieldSuffix}.set${propertyDesc.name?cap_first}(${propertyDesc.name});<#else>${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix} = ${propertyDesc.name};</#if>
    }
</#if>
</#list>
<#list classDesc.methodDescs as methodDesc>

<#list methodDesc.annotationDescs as annotationDesc>
    ${annotationDesc.asShortString}
</#list>
    public ${methodDesc.returnTypeDesc.shortName} ${methodDesc.name}(<#list methodDesc.parameterDescs as parameterDesc>${parameterDesc.typeDesc.shortName} <#if parameterDesc.nameAsIs??>${parameterDesc.nameAsIs}<#else>arg${parameterDesc_index}</#if><#if parameterDesc_has_next>, </#if></#list>)<#if !methodDesc.throwsDesc.empty>
        throws </#if><#list methodDesc.throwsDesc.throwableClassShortNames as throwableClassShortName>${throwableClassShortName}<#if throwableClassShortName_has_next>, </#if></#list> {
<#if methodDesc.evaluatedBody?exists>
        ${methodDesc.evaluatedBody}<#elseif methodDesc.returnTypeDesc.shortName != "void">
        return ${methodDesc.returnTypeDesc.defaultValue};</#if>
    }
</#list>
}
