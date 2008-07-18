<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

import org.seasar.ymir.mine.Converter;

import ${targetClassDesc.name};
<#list pairClassDescs as pairClassDesc>
import ${pairClassDesc.name};
</#list>

public class ${classDesc.shortName}Base extends Converter
{
<#list pairClassDescs as pairClassDesc>
    public ${targetClassDesc.shortName} copyTo(${targetClassDesc.shortName} dto, ${pairClassDesc.shortName} entity)
    {
        dto = copyingTo(dto, entity);

<#list pairClassDesc.propertyDescs as propertyDesc>
        copy${propertyDesc.name?cap_first}To(dto, entity);
</#list>

        return copiedTo(dto, entity);
    }

    protected ${targetClassDesc.shortName} copyingTo(${targetClassDesc.shortName} dto, ${pairClassDesc.shortName} entity)
    {
        return dto;
    }

<#list pairClassDesc.propertyDescs as propertyDesc>
    protected void copy${propertyDesc.name?cap_first}To(${targetClassDesc.shortName} dto, ${pairClassDesc.shortName} entity)
    {
        dto.set${propertyDesc.name?cap_first}(convert(entity.get${propertyDesc.name?cap_first}()));
    }

</#list>
    protected ${targetClassDesc.shortName} copiedTo(${targetClassDesc.shortName} dto, ${pairClassDesc.shortName} entity)
    {
        return dto;
    }

    public ${pairClassDesc.shortName} copyTo(${pairClassDesc.shortName} entity, ${targetClassDesc.shortName} dto)
    {
        entity = copyingTo(entity, dto);

<#list pairClassDesc.propertyDescs as propertyDesc>
        copy${propertyDesc.name?cap_first}To(entity, dto);
</#list>

        return copiedTo(entity, dto);
    }

    protected ${pairClassDesc.shortName} copyingTo(${pairClassDesc.shortName} entity, ${targetClassDesc.shortName} dto)
    {
        return entity;
    }

<#list pairClassDesc.propertyDescs as propertyDesc>
    protected void copy${propertyDesc.name?cap_first}To(${pairClassDesc.shortName} entity, ${targetClassDesc.shortName} dto)
    {
        entity.set${propertyDesc.name?cap_first}(convert(dto.get${propertyDesc.name?cap_first}(), (${propertyDesc.typeDesc.name}) null));
    }

</#list>
    protected ${pairClassDesc.shortName} copiedTo(${pairClassDesc.shortName} entity, ${targetClassDesc.shortName} dto)
    {
        return entity;
    }

</#list>
}
