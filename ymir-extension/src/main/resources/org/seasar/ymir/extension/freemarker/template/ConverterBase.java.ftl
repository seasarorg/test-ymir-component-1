${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

import ${application.rootPackageName}.converter.Converter;

import ${targetClassDesc.name};
<#list pairClassDescs as pairClassDesc><#if pairClassDesc.name != "java.lang.Object">
import ${pairClassDesc.name};
</#if></#list>

public class ${classDesc.shortName}Base extends Converter
{
<#list pairClassDescs as pairClassDesc><#if pairClassDesc.name != "java.lang.Object">
    public ${targetClassDesc.shortName} copyTo(${targetClassDesc.shortName} dto, ${pairClassDesc.shortName} entity)
    {
<#list targetClassDesc.propertyDescs as propertyDesc><#if pairClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = pairClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
        copy${propertyDesc.name?cap_first}To(dto, entity);
</#if></#if></#list>

        return dto;
    }
<#list targetClassDesc.propertyDescs as propertyDesc><#if pairClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = pairClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>

    protected void copy${propertyDesc.name?cap_first}To(${targetClassDesc.shortName} dto, ${pairClassDesc.shortName} entity)
    {
        dto.set${propertyDesc.name?cap_first}(convert(entity.${pd.getterName}(), ${propertyDesc.getTypeDesc().getName()}.class));
    }
</#if></#if></#list>

    public ${pairClassDesc.shortName} copyTo(${pairClassDesc.shortName} entity, ${targetClassDesc.shortName} dto)
    {
<#list pairClassDesc.propertyDescs as propertyDesc><#if targetClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = targetClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
        copy${propertyDesc.name?cap_first}To(entity, dto);
</#if></#if></#list>

        return entity;
    }
<#list pairClassDesc.propertyDescs as propertyDesc><#if targetClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = targetClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>

    protected void copy${propertyDesc.name?cap_first}To(${pairClassDesc.shortName} entity, ${targetClassDesc.shortName} dto)
    {
        entity.set${propertyDesc.name?cap_first}(convert(dto.${pd.getterName}(), ${propertyDesc.typeDesc.name}.class));
    }
</#if></#if></#list>

</#if></#list>
}
