${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>
<#if !importDesc.empty>

${importDesc.asString}</#if>
<#list classDesc.annotationDescs as annotationDesc>
${annotationDesc.asShortString}
</#list>
public class ${classDesc.shortName}Base
    implements Serializable<#list classDesc.interfaceTypeDescs as interfaceTypeDesc>, ${interfaceTypeDesc.shortName}</#list> {
    private static final long serialVersionUID = 1L;

<#list classDesc.propertyDescs as propertyDesc>
<#list propertyDesc.annotationDescs as annotationDesc>
    ${annotationDesc.asShortString}
</#list>
    protected ${propertyDesc.typeDesc.shortName} ${fieldPrefix}${propertyDesc.name}${fieldSuffix}<#if propertyDesc.initialValue??> = ${propertyDesc.initialValue}</#if>;

</#list>

    public ${classDesc.shortName}Base() {
    }

    public ${classDesc.shortName}Base(<#list classDesc.propertyDescsOrderByName as propertyDesc>${propertyDesc.typeDesc.shortName} ${propertyDesc.name}<#if propertyDesc_has_next>, </#if></#list>) {
<#list classDesc.propertyDescsOrderByName as propertyDesc>
        ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix} = ${propertyDesc.name};
</#list>
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append('(');<#list classDesc.propertyDescs as propertyDesc>
        append(sb.append("${propertyDesc.name}="), ${fieldPrefix}${propertyDesc.name}${fieldSuffix})<#if propertyDesc_has_next>.append(", ")</#if>;</#list>
        sb.append(')');
        return toString(sb);
    }

    protected StringBuilder append(StringBuilder sb, Object obj) {
        if (obj != null && obj.getClass().isArray()) {
            sb.append('{');
            int len = java.lang.reflect.Array.getLength(obj);
            String delim = "";
            for (int i = 0; i < len; i++) {
                sb.append(delim);
                delim = ", ";
                append(sb, java.lang.reflect.Array.get(obj, i));
            }
            sb.append('}');
        } else {
            sb.append(obj);
        }
        return sb;
    }

    protected String toString(StringBuilder sb) {
        return sb.toString();
    }
<#list classDesc.propertyDescs as propertyDesc>
<#if propertyDesc.readable>

<#list propertyDesc.annotationDescsOnGetter as annotationDesc>
    ${annotationDesc.asShortString}
</#list>
    public ${propertyDesc.typeDesc.shortName} <#if propertyDesc.typeDesc.shortName == "boolean">is<#else>get</#if>${propertyDesc.name?cap_first}() {
        return ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix};
    }
</#if>
<#if propertyDesc.writable>

<#list propertyDesc.annotationDescsOnSetter as annotationDesc>
    ${annotationDesc.asShortString}
</#list>
    public void set${propertyDesc.name?cap_first}(${propertyDesc.typeDesc.shortName} ${propertyDesc.name}) {
        ${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix} = ${propertyDesc.name};
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
