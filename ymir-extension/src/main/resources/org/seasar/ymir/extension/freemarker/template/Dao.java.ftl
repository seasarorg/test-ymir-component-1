${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>
<#if !importDesc.empty>

${importDesc.asString}</#if>
@S2Dao(bean = ${entityMetaData.beanClassDesc.shortName}.class)
public interface ${classDesc.shortName} {
    ${entityMetaData.beanClassDesc.shortName}[] selectAll();

    int insert(${entityMetaData.beanClassDesc.shortName} ${entityMetaData.beanClassDesc.instanceName});

    int update(${entityMetaData.beanClassDesc.shortName} ${entityMetaData.beanClassDesc.instanceName});

    int delete(${entityMetaData.beanClassDesc.shortName} ${entityMetaData.beanClassDesc.instanceName});
}
