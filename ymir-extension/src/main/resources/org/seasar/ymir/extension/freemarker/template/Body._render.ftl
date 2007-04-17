<#list propertyDescs as propertyDesc>

${fieldSpecialPrefix}${fieldPrefix}${propertyDesc.name}${fieldSuffix} = ${fieldSpecialPrefix}${fieldPrefix}${entityMetaData.dxoClassDesc.instanceName}${fieldSuffix}.convert(${fieldSpecialPrefix}${fieldPrefix}${entityMetaData.daoClassDesc.instanceName}${fieldSuffix}.selectAll());
</#list>
