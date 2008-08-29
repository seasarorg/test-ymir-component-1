${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

import java.io.Serializable;

<#list classDesc.annotationDescs as annotationDesc>${annotationDesc.string}
</#list>public class ${classDesc.shortName}Base
    implements Serializable
{
    private static final long serialVersionUID = 1L;

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

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder().append('(');<#list classDesc.propertyDescs as propertyDesc>
        append(sb.append("${propertyDesc.name}="), ${fieldPrefix}${propertyDesc.name}${fieldSuffix})<#if propertyDesc_has_next>.append(", ");</#if></#list>
        sb.append(')');
        return toString(sb);
    }

    protected StringBuilder append(StringBuilder sb, Object obj)
    {
        if (obj != null && obj.getClass().isArray()) {
            sb.append('{');
            int len = Array.getLength(obj);
            String delim = "";
            for (int i = 0; i < len; i++) {
                sb.append(delim);
                delim = ", ";
                append(sb, Array.get(obj, i));
            }
            sb.append('}');
        } else {
            sb.append(obj);
        }
        return sb;
    }

    protected String toString(StringBuilder sb)
    {
        return sb.toString();
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
