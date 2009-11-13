${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>
<#if !importDesc.empty>

${importDesc.asString}</#if>
public class ${classDesc.shortName} extends ${classDesc.shortName}Base
    implements Serializable {
    private static final long serialVersionUID = 1L;

    public ${classDesc.shortName}() {
    }
}
