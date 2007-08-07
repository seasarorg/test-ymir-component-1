<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

import org.seasar.dao.annotation.tiger.S2Dao;

@S2Dao(bean = ${entityMetaData.beanClassDesc.shortName}.class)
public interface ${classDesc.shortName}
{
    ${entityMetaData.beanClassDesc.shortName}[] selectAll();

    int insert(${entityMetaData.beanClassDesc.shortName} ${entityMetaData.beanClassDesc.instanceName});

    int update(${entityMetaData.beanClassDesc.shortName} ${entityMetaData.beanClassDesc.instanceName});

    int delete(${entityMetaData.beanClassDesc.shortName} ${entityMetaData.beanClassDesc.instanceName});
}
