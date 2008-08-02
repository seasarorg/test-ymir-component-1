${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

import ${application.rootPackageName}.converter.Converter;

<#if pairTypeDescs?size &gt; 0>import ${targetClassDesc.name};
</#if><#list pairTypeDescs as pairTypeDesc><#list pairTypeDesc.importClassNames as importClassName>
import ${importClassName};
</#list></#list>

public class ${classDesc.shortName}Base extends Converter
{
<#list pairTypeDescs as pairTypeDesc>
    public ${targetClassDesc.shortName} copyTo(${targetClassDesc.shortName} dto, ${pairTypeDesc.shortName} entity)
    {
<#list targetClassDesc.propertyDescs as propertyDesc><#if pairTypeDesc.classDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = pairTypeDesc.classDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
        copy${propertyDesc.name?cap_first}To(dto, entity);
</#if></#if></#list>

        return dto;
    }
<#list targetClassDesc.propertyDescs as propertyDesc><#if pairTypeDesc.classDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = pairTypeDesc.classDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>

    protected void copy${propertyDesc.name?cap_first}To(${targetClassDesc.shortName} dto, ${pairTypeDesc.shortName} entity)
    {
        dto.set${propertyDesc.name?cap_first}(convert(entity.${pd.getterName}(), ${propertyDesc.getTypeDesc().getName()}.class));
    }
</#if></#if></#list>

    public ${pairTypeDesc.shortName} copyTo(${pairTypeDesc.shortName} entity, ${targetClassDesc.shortName} dto)
    {
<#list pairTypeDesc.classDesc.propertyDescs as propertyDesc><#if targetClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = targetClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
        copy${propertyDesc.name?cap_first}To(entity, dto);
</#if></#if></#list>

        return entity;
    }
<#list pairTypeDesc.classDesc.propertyDescs as propertyDesc><#if targetClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = targetClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>

    protected void copy${propertyDesc.name?cap_first}To(${pairTypeDesc.shortName} entity, ${targetClassDesc.shortName} dto)
    {
        entity.set${propertyDesc.name?cap_first}(convert(dto.${pd.getterName}(), ${propertyDesc.typeDesc.name}.class));
    }
</#if></#if></#list>

</#list>
}
