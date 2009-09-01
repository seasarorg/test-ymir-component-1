${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>
<#if classDesc.superclassShortName?? && classDesc.superclassShortName != classDesc.shortName>

import ${classDesc.superclassName};
</#if>

public class ${classDesc.shortName}<#if classDesc.superclassShortName?? && classDesc.superclassShortName != "Object"> extends <#if classDesc.superclassShortName != classDesc.shortName>${classDesc.superclassShortName}<#else>${classDesc.superclassName}</#if></#if> {
}
