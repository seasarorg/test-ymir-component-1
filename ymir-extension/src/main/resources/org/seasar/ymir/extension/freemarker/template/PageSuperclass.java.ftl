${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

public class ${classDesc.shortName}<#if classDesc.superclassShortName?exists && classDesc.superclassShortName != "Object"> extends ${classDesc.superclassShortName}</#if> {
}
