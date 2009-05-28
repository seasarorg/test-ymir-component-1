${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>
<#if !importDesc.empty>

${importDesc.asString}</#if>
<#list classDesc.annotationDescs as annotationDesc>
${annotationDesc.asShortString}
</#list>
public <#if classDesc.abstract>abstract </#if>class ${classDesc.shortName}Base<#if classDesc.superclassShortName??> extends ${classDesc.superclassShortName}</#if><#list classDesc.interfaceTypeDescs as interfaceTypeDesc><#if interfaceTypeDesc_index == 0>
    implements </#if>${interfaceTypeDesc.shortName}<#if interfaceTypeDesc_has_next>, </#if></#list> {
    public static final String PACKAGE = "${classDesc.packageName}";

    public static final String NAME = "${classDesc.nameBase?uncap_first}";
<#if classDesc.pathOfClass??>

    public static final String PATH = "${classDesc.pathOfClass}";
</#if>
<#list classDesc.propertyDescs as propertyDesc>

    public static final String P_${propertyDesc.name} = "${propertyDesc.name}";
  <#if propertyDesc.typeDesc.name == "org.seasar.ymir.render.Selector"
       && propertyDesc.name?ends_with("Selector")>

    public static final String P_${propertyDesc.name}_selectedValues = "${propertyDesc.name}.selectedValues";
  <#elseif propertyDesc.typeDesc.name == "org.seasar.ymir.render.html.Select"
       && propertyDesc.name?ends_with("Select")>

    public static final String P_${propertyDesc.name}_value = "${propertyDesc.name}.value";
  </#if>
</#list>
<#list classDesc.propertyDescs as propertyDesc>
<#if !propertyDesc.hasMetaOnGetterOrSetter("formProperty")>

<#list propertyDesc.annotationDescs as annotationDesc>
    ${annotationDesc.asShortString}
</#list>
    protected ${propertyDesc.typeDesc.shortName} ${fieldPrefix}${propertyDesc.name}${fieldSuffix}<#if propertyDesc.initialShortValue??> = ${propertyDesc.initialShortValue}</#if>;
</#if>
</#list>
<#list classDesc.propertyDescs as propertyDesc>
<#if propertyDesc.readable>

<#list propertyDesc.annotationDescsOnGetter as annotationDesc>
    ${annotationDesc.asShortString}
</#list>
    public ${propertyDesc.typeDesc.shortName} <#if propertyDesc.typeDesc.shortName == "boolean">is<#else>get</#if>${propertyDesc.name?cap_first}() {
        return <#if propertyDesc.hasMetaOnGetterOrSetter("formProperty")>${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.getMetaFirstValueOnGetterOrSetter("formProperty")}${fieldSuffix}.<#if propertyDesc.typeDesc.shortName == "boolean">is<#else>get</#if>${propertyDesc.name?cap_first}();<#else>${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix};</#if>
    }
</#if>
<#if propertyDesc.writable>

<#list propertyDesc.annotationDescsOnSetter as annotationDesc>
    ${annotationDesc.asShortString}
</#list>
    public void set${propertyDesc.name?cap_first}(${propertyDesc.typeDesc.shortName} ${propertyDesc.name}) {
        <#if propertyDesc.hasMetaOnGetterOrSetter("formProperty")>${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.getMetaFirstValueOnGetterOrSetter("formProperty")}${fieldSuffix}.set${propertyDesc.name?cap_first}(${propertyDesc.name});<#else>${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix} = ${propertyDesc.name};</#if>
    }
</#if>
</#list>
<#list classDesc.methodDescs as methodDesc>
  <#if methodDesc.getAttribute("action")??>
    <#assign actionInterface = methodDesc.getAttribute("action.interface.shortName")>
    <#assign actionKey = methodDesc.getAttribute("action.key")>

    public static interface ${methodDesc.name} extends ${actionInterface} {
        public static final String NAME = "${methodDesc.name}";

        public static final String KEY = "${actionKey}";

        public static final Class<? extends ${actionInterface}> method = ${methodDesc.name}.class;
    }
</#if>

<#list methodDesc.annotationDescs as annotationDesc>
    ${annotationDesc.asShortString}
</#list>
    public ${methodDesc.returnTypeDesc.shortName} ${methodDesc.name}(<#list methodDesc.parameterDescs as parameterDesc>${parameterDesc.typeDesc.shortName} <#if parameterDesc.nameAsIs??>${parameterDesc.nameAsIs}<#else>arg${parameterDesc_index}</#if><#if parameterDesc_has_next>, </#if></#list>)<#if !methodDesc.throwsDesc.empty>
        throws </#if><#list methodDesc.throwsDesc.throwableClassShortNames as throwableClassShortName>${throwableClassShortName}<#if throwableClassShortName_has_next>, </#if></#list> {
<#if methodDesc.getAttribute("evaluatedBody")?exists>
        ${methodDesc.getAttribute("evaluatedBody")}<#elseif methodDesc.returnTypeDesc.shortName != "void">
        return ${methodDesc.returnTypeDesc.defaultValue};</#if>
    }
</#list>
}
