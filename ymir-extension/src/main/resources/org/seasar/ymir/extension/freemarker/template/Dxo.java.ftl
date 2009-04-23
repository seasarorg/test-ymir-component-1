${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>
<#if !importDesc.empty>

${importDesc.asString}</#if>
public interface ${classDesc.shortName} {
    ${entityMetaData.dtoClassDesc.shortName} convert(${entityMetaData.beanClassDesc.shortName} ${entityMetaData.beanClassDesc.instanceName});

    ${entityMetaData.beanClassDesc.shortName} convert(${entityMetaData.dtoClassDesc.shortName} ${entityMetaData.dtoClassDesc.instanceName});

    void convert(${entityMetaData.dtoClassDesc.shortName} src, ${entityMetaData.beanClassDesc.shortName} dest);

    void convert(${entityMetaData.beanClassDesc.shortName} src, ${entityMetaData.dtoClassDesc.shortName} dest);

    ${entityMetaData.dtoClassDesc.shortName}[] convert(${entityMetaData.beanClassDesc.shortName}[] ${entityMetaData.beanClassDesc.instanceName}s);

    ${entityMetaData.dtoClassDesc.shortName}[] convert(List list);

    ${entityMetaData.beanClassDesc.shortName}[] convert(${entityMetaData.dtoClassDesc.shortName}[] ${entityMetaData.dtoClassDesc.instanceName}s);
<#list classDesc.methodDescs as methodDesc>

    ${methodDesc.returnTypeDesc.name} ${methodDesc.name}(<#list methodDesc.parameterDescs as parameterDesc>${parameterDesc.typeDesc.shortName} <#if parameterDesc.nameAsIs??>${parameterDesc.nameAsIs}<#else>arg${parameterDesc_index}</#if><#if parameterDesc_has_next>, </#if></#list>);
</#list>
}
