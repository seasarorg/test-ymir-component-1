${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

public class ${classDesc.shortName}<#if classDesc.superclassName?exists && classDesc.superclassName != "java.lang.Object"> extends ${classDesc.superclassName}</#if>
{
}
