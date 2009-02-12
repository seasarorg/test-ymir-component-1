${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

import org.seasar.ymir.beantable.annotation.Managed;

@Managed
public class ${classDesc.shortName} extends ${classDesc.shortName}Base {
    public ${classDesc.shortName}() {
    }

    public ${classDesc.shortName}(<#list classDesc.propertyDescsOrderByName as propertyDesc>${propertyDesc.typeDesc.name} ${propertyDesc.name}<#if propertyDesc_has_next>, </#if></#list>) {
        super(<#list classDesc.propertyDescsOrderByName as propertyDesc>${propertyDesc.name}<#if propertyDesc_has_next>, </#if></#list>);
    }
}
