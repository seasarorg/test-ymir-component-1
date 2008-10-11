${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

<#if pairTypeDescs?size &gt; 0>
import java.util.ArrayList;
import java.util.List;

</#if>
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.message.Messages;

<#if pairTypeDescs?size &gt; 0>import ${targetClassDesc.name};
</#if><#list pairTypeDescs as pairTypeDesc><#list pairTypeDesc.importClassNames as importClassName>
import ${importClassName};
</#list></#list>

public class ${classDesc.shortName}Base
{
    protected TypeConversionManager ${fieldPrefix}typeConversionManager${fieldSuffix};

    protected Messages ${fieldPrefix}messages${fieldSuffix};

    @Binding(bindingType = BindingType.MUST)
    final public void setConversionManager(
        TypeConversionManager typeConversionManager) {
        ${fieldSpecialPrefix}${fieldPrefix}typeConversionManager${fieldSuffix} = typeConversionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    final public void setMessages(Messages messages) {
        ${fieldSpecialPrefix}${fieldPrefix}messages${fieldSuffix} = messages;
    }

    final protected TypeConversionManager getTypeConversionManager() {
        return ${fieldPrefix}typeConversionManager${fieldSuffix};
    }

    final protected Messages getMessages() {
        return ${fieldPrefix}messages${fieldSuffix};
    }

    final protected <T> T convert(Object value, Class<T> type) {
        return ${fieldPrefix}typeConversionManager${fieldSuffix}.convert(value, type);
    }

    final protected String valueOf(Object obj) {
        if (obj == null) {
            return null;
        }
        return String.valueOf(obj);
    }

    final protected boolean isEmpty(Object obj) {
        return (obj == null || obj instanceof String
            && ((String) obj).trim().length() == 0);
    }

    final protected <T> T emptyToNull(T obj) {
        if (isEmpty(obj)) {
            return null;
        } else {
            return obj;
        }
    }

<#list pairTypeDescs as pairTypeDesc>
    public ${targetClassDesc.shortName} copyTo(${targetClassDesc.shortName} dto, ${pairTypeDesc.shortName} entity)
    {
<#list targetClassDesc.propertyDescs as propertyDesc><#if pairTypeDesc.classDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = pairTypeDesc.classDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
        copy${propertyDesc.name?cap_first}To(dto, entity);
</#if></#if></#list>

        return dto;
    }

    public ${targetClassDesc.shortName}[] copyTo(${pairTypeDesc.shortName}[] entities)
    {
        ${targetClassDesc.shortName}[] dtos = new ${targetClassDesc.shortName}[entities.length];
        for (int i = 0; i < entities.length; i++) {
            dtos[i] = copyTo(new ${targetClassDesc.shortName}(), entities[i]);
        }
        return dtos;
    }

    public List<${targetClassDesc.shortName}> copyToDtoList(List<${pairTypeDesc.shortName}> entityList)
    {
        List<${targetClassDesc.shortName}> dtoList = new ArrayList<${targetClassDesc.shortName}>();
        for (${pairTypeDesc.shortName} entity : entityList) {
            dtoList.add(copyTo(new ${targetClassDesc.shortName}(), entity));
        }
        return dtoList;
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

    public ${pairTypeDesc.shortName}[] copyTo(${targetClassDesc.shortName}[] dtos)
    {
<#if pairTypeDesc.generic>        @SuppressWarnings("unchecked")</#if>
        ${pairTypeDesc.shortName}[] entities = new ${pairTypeDesc.shortClassName}[dtos.length];
        for (int i = 0; i < dtos.length; i++) {
            entities[i] = copyTo(new ${pairTypeDesc.shortName}(), dtos[i]);
        }
        return entities;
    }

    public List<${pairTypeDesc.shortName}> copyToEntityList(List<${targetClassDesc.shortName}> dtoList)
    {
        List<${pairTypeDesc.shortName}> entityList = new ArrayList<${pairTypeDesc.shortName}>();
        for (${targetClassDesc.shortName} dto : dtoList) {
            entityList.add(copyTo(new ${pairTypeDesc.shortName}(), dto));
        }
        return entityList;
    }
<#list pairTypeDesc.classDesc.propertyDescs as propertyDesc><#if targetClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = targetClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>

    protected void copy${propertyDesc.name?cap_first}To(${pairTypeDesc.shortName} entity, ${targetClassDesc.shortName} dto)
    {
        entity.set${propertyDesc.name?cap_first}(convert(dto.${pd.getterName}(), ${propertyDesc.typeDesc.name}.class));
    }
</#if></#if></#list>

</#list>
}
