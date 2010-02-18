${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>
<#if importDesc.containsClass("java.io.Serializable")>

import java.io.Serializable;
</#if>

public class ${classDesc.shortName}<#if classDesc.superclassShortName?? && classDesc.superclassShortName != "Object"> extends <#if classDesc.superclassShortName != classDesc.shortName>${classDesc.superclassShortName}<#else>${classDesc.superclassName}</#if></#if><#if importDesc.containsClass("java.io.Serializable")>
    implements Serializable</#if> {
<#if importDesc.containsClass("java.io.Serializable")>
    private static final long serialVersionUID = 1L;
</#if>
}
