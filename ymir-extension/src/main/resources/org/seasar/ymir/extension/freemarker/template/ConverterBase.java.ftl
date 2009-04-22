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
</#if><#list pairTypeDescs as pairTypeDesc><#list pairTypeDesc.dependingClassNames as dependingClassName>
import ${dependingClassName};
</#list></#list>

/**
 * A class to convert Dto objects and entity objects.
 * <p>Developer can override methods to customize this class's behavior,
 * and add methods to gain conversion ability.
 * </p>
 */
<#list classDesc.annotationDescs as annotationDesc>${annotationDesc.string}
</#list>public class ${classDesc.shortName}Base {
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

    /**
     * Gets TypeConversionManager.
     * <p>Gets a TypeConversionManager instance to convert types of object.
     * </p>
     * 
     * @return A TypeConversionManager instance.
     */
    final protected TypeConversionManager getTypeConversionManager() {
        return ${fieldPrefix}typeConversionManager${fieldSuffix};
    }

    /**
     * Gets Messages.
     * <p>Gets a Messages instance to localize messages represented by keys.
     * </p>
     * 
     * @return A Messages instance.
     */
    final protected Messages getMessages() {
        return ${fieldPrefix}messages${fieldSuffix};
    }

    /**
     * Converts object by specified type.
     * <p>Converts object by specified type using TypeConversionManager.
     * </p>
     * 
     * @param value Source object.
     * @param type Destination type.
     * @return Conversion result.
     */
    protected <T> T convert(Object value, Class<T> type) {
        return ${fieldPrefix}typeConversionManager${fieldSuffix}.convert(value, type);
    }

    /**
     * Converts object for entity by specified type.
     * <p>This method is used in order to set converted objects to entities.
     * You can change behavior of this method by overriding this method.
     * </p>
     * 
     * @param value source object.
     * @param type destination type.
     * @return conversion result.
     */
    protected <T> T convertForEntity(Object value, Class<T> type) {
        return ${fieldPrefix}typeConversionManager${fieldSuffix}.convert(value, type);
    }

    /**
     * Gets String representation of object.
     * <p>Gets String representation of specified object.
     * If the object is null, null is returned.
     * </p>
     * 
     * @param obj Source object.
     * @return String representation.
     */
    final protected String valueOf(Object obj) {
        if (obj == null) {
            return null;
        }
        return String.valueOf(obj);
    }

    /**
     * Returns whether object is empty or not.
     * <p>Returns whether specified object is empty or not.
     * 'Empty' means that a object is null or 0-length String.
     * </p>
     *  
     * @param obj Target object.
     * @return Whether object is empty or not.
     */
    protected boolean isEmpty(Object obj) {
        return (obj == null || obj instanceof String
            && ((String) obj).trim().length() == 0);
    }

    /**
     * Converts null if object is empty.
     * <p>Converts null if specified object is empty.
     * 'Empty' means that a object is null or 0-length String.
     * </p>
     *  
     * @param obj Target object.
     * @return Original object, or null if it is empty.
     */
    protected <T> T emptyToNull(T obj) {
        if (isEmpty(obj)) {
            return null;
        } else {
            return obj;
        }
    }

<#list pairTypeDescs as pairTypeDesc>
    /**
     * Copies ${pairTypeDesc.shortName} entity to ${targetClassDesc.shortName} instance.
     * <p>This methods copies the following properties automatically:</p>
     * <ul>
<#list targetClassDesc.propertyDescs as propertyDesc><#if pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
     *   <li>${propertyDesc.name}</li>
</#if></#if></#list>
     * </ul>
     * 
     * @param entity Source object.
     * @param dto Destination object.
     * @return The first argument of this method.
     */
    public ${targetClassDesc.shortName} copyEntityToDto(${pairTypeDesc.shortName} entity, ${targetClassDesc.shortName} dto) {
<#list targetClassDesc.propertyDescs as propertyDesc><#if pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
        reflect${propertyDesc.name?cap_first}ToDto(entity, dto);
</#if></#if></#list>

        return dto;
    }

    /**
     * Copies a List of ${pairTypeDesc.shortName} entity to a List of ${targetClassDesc.shortName}.
     * <p>This methods copies the following properties automatically:</p>
     * <ul>
<#list targetClassDesc.propertyDescs as propertyDesc><#if pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
     *   <li>${propertyDesc.name}</li>
</#if></#if></#list>
     * </ul>
     * 
     * @param entityList Source List.
     * @return Copied List of Dto.
     */
    public List<${targetClassDesc.shortName}> toDtoList(List<${pairTypeDesc.shortName}> entityList) {
        List<${targetClassDesc.shortName}> dtoList = new ArrayList<${targetClassDesc.shortName}>();
        for (${pairTypeDesc.shortName} entity : entityList) {
            dtoList.add(copyEntityToDto(entity, new ${targetClassDesc.shortName}()));
        }
        return dtoList;
    }
<#list targetClassDesc.propertyDescs as propertyDesc><#if pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>

    /**
     * Copies '${propertyDesc.name}' property of ${pairTypeDesc.shortName} entity to ${targetClassDesc.shortName} instance's '${propertyDesc.name}' property.
     * 
     * @param entity Source object.
     * @param dto Destination object.
     */
    protected void reflect${propertyDesc.name?cap_first}ToDto(${pairTypeDesc.shortName} entity, ${targetClassDesc.shortName} dto) {
        dto.set${propertyDesc.name?cap_first}(extract${propertyDesc.name?cap_first}FromEntity(entity));
    }

    /**
     * Extracts '${propertyDesc.name}' property of ${pairTypeDesc.shortName} entity in order to copy to ${targetClassDesc.shortName} instance's '${propertyDesc.name}' property.
     * 
     * @param entity Source object.
     * @return Extracted value.
     */
    protected ${propertyDesc.typeDesc.name} extract${propertyDesc.name?cap_first}FromEntity(${pairTypeDesc.shortName} entity) {
        return convert(entity.${pd.getterName}(), ${propertyDesc.typeDesc.name}.class);
    }
</#if></#if></#list>

    /**
     * Copies ${targetClassDesc.shortName} entity to ${pairTypeDesc.shortName} instance.
     * <p>This methods copies the following properties automatically:</p>
     * <ul>
<#list pairTypeDesc.componentClassDesc.propertyDescs as propertyDesc><#if targetClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = targetClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
     *   <li>${propertyDesc.name}</li>
</#if></#if></#list>
     * </ul>
     * 
     * @param dto Source object.
     * @param entity Destination object.
     * @return The first argument of this method.
     */
    public ${pairTypeDesc.shortName} copyDtoToEntity(${targetClassDesc.shortName} dto, ${pairTypeDesc.shortName} entity) {
<#list pairTypeDesc.componentClassDesc.propertyDescs as propertyDesc><#if targetClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = targetClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
        reflect${propertyDesc.name?cap_first}ToEntity(dto, entity);
</#if></#if></#list>

        return entity;
    }

    /**
     * Copies a List of ${targetClassDesc.shortName} entity to a List of ${pairTypeDesc.shortName}.
     * <p>This methods copies the following properties automatically:</p>
     * <ul>
<#list pairTypeDesc.componentClassDesc.propertyDescs as propertyDesc><#if targetClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = targetClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
     *   <li>${propertyDesc.name}</li>
</#if></#if></#list>
     * </ul>
     * 
     * @param dtoList Source List.
     * @return Copied List of entity.
     */
    public List<${pairTypeDesc.shortName}> toEntityList(List<${targetClassDesc.shortName}> dtoList) {
        List<${pairTypeDesc.shortName}> entityList = new ArrayList<${pairTypeDesc.shortName}>();
        for (${targetClassDesc.shortName} dto : dtoList) {
            entityList.add(copyDtoToEntity(dto, new ${pairTypeDesc.shortName}()));
        }
        return entityList;
    }
<#list pairTypeDesc.componentClassDesc.propertyDescs as propertyDesc><#if targetClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = targetClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>

    /**
     * Copies '${propertyDesc.name}' property of ${targetClassDesc.shortName} entity to ${pairTypeDesc.shortName} instance's '${propertyDesc.name}' property.
     * 
     * @param dto Source object.
     * @param entity Destination object.
     */
    protected void reflect${propertyDesc.name?cap_first}ToEntity(${targetClassDesc.shortName} dto, ${pairTypeDesc.shortName} entity) {
        entity.set${propertyDesc.name?cap_first}(extract${propertyDesc.name?cap_first}FromDto(dto));
    }

    /**
     * Extracts '${propertyDesc.name}' property of ${targetClassDesc.shortName} entity in order to copy to ${pairTypeDesc.shortName} instance's '${propertyDesc.name}' property.
     * 
     * @param dto Source object.
     * @return Extracted value.
     */
    protected ${propertyDesc.typeDesc.name} extract${propertyDesc.name?cap_first}FromDto(${targetClassDesc.shortName} dto) {
        return convertForEntity(dto.${pd.getterName}(), ${propertyDesc.typeDesc.name}.class);
    }
</#if></#if></#list>

</#list>
}
