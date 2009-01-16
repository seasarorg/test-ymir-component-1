${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

import java.io.Serializable;

public class ${classDesc.shortName} extends ${classDesc.shortName}Base
    implements Serializable {
    private static final long serialVersionUID = 1L;

    public ${classDesc.shortName}() {
    }
}
