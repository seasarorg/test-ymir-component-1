${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

import java.io.Serializable;

public class ${classDesc.shortName} extends ${classDesc.shortName}Base
    implements Serializable
{
    public ${classDesc.shortName}()
    {
    }
}
