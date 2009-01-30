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

/**
 * A class to convert Dto objects and entity objects.
 * <p>Developer can override methods to customize this class's behavior,
 * and add methods to gain conversion ability.
 * </p>
 */
public class ${classDesc.shortName}Base {
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
     * @param dto Destination object.
     * @param entity Source object.
     * @return The first argument of this method.
     */
    public ${targetClassDesc.shortName} copyTo(${targetClassDesc.shortName} dto, ${pairTypeDesc.shortName} entity) {
<#list targetClassDesc.propertyDescs as propertyDesc><#if pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
        copy${propertyDesc.name?cap_first}To(dto, entity);
</#if></#if></#list>

        return dto;
    }

    /**
     * Copies an array of ${pairTypeDesc.shortName} entity to an array of ${targetClassDesc.shortName}.
     * <p>This methods copies the following properties automatically:</p>
     * <ul>
<#list targetClassDesc.propertyDescs as propertyDesc><#if pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
     *   <li>${propertyDesc.name}</li>
</#if></#if></#list>
     * </ul>
     * 
     * @param entities Source array.
     * @return Copied array of Dto.
     */
    public ${targetClassDesc.shortName}[] copyTo(${pairTypeDesc.shortName}[] entities) {
        ${targetClassDesc.shortName}[] dtos = new ${targetClassDesc.shortName}[entities.length];
        for (int i = 0; i < entities.length; i++) {
            dtos[i] = copyTo(new ${targetClassDesc.shortName}(), entities[i]);
        }
        return dtos;
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
    public List<${targetClassDesc.shortName}> copyToDtoList(List<${pairTypeDesc.shortName}> entityList) {
        List<${targetClassDesc.shortName}> dtoList = new ArrayList<${targetClassDesc.shortName}>();
        for (${pairTypeDesc.shortName} entity : entityList) {
            dtoList.add(copyTo(new ${targetClassDesc.shortName}(), entity));
        }
        return dtoList;
    }
<#list targetClassDesc.propertyDescs as propertyDesc><#if pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = pairTypeDesc.componentClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>

    /**
     * Copies '${propertyDesc.name}' property of ${pairTypeDesc.shortName} entity to ${targetClassDesc.shortName} instance's '${propertyDesc.name}' property.
     * 
     * @param dto Destination object.
     * @param entity Source object.
     */
    protected void copy${propertyDesc.name?cap_first}To(${targetClassDesc.shortName} dto, ${pairTypeDesc.shortName} entity) {
        dto.set${propertyDesc.name?cap_first}(convert(entity.${pd.getterName}(), ${propertyDesc.getTypeDesc().getName()}.class));
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
     * @param entity Destination object.
     * @param dto Source object.
     * @return The first argument of this method.
     */
    public ${pairTypeDesc.shortName} copyTo(${pairTypeDesc.shortName} entity, ${targetClassDesc.shortName} dto) {
<#list pairTypeDesc.componentClassDesc.propertyDescs as propertyDesc><#if targetClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = targetClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
        copy${propertyDesc.name?cap_first}To(entity, dto);
</#if></#if></#list>

        return entity;
    }

    /**
     * Copies an array of ${targetClassDesc.shortName} entity to an array of ${pairTypeDesc.shortName}.
     * <p>This methods copies the following properties automatically:</p>
     * <ul>
<#list pairTypeDesc.componentClassDesc.propertyDescs as propertyDesc><#if targetClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = targetClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>
     *   <li>${propertyDesc.name}</li>
</#if></#if></#list>
     * </ul>
     * 
     * @param dtos Source array.
     * @return Copied array of entity.
     */
    public ${pairTypeDesc.shortName}[] copyTo(${targetClassDesc.shortName}[] dtos) {
<#if pairTypeDesc.generic>        @SuppressWarnings("unchecked")</#if>
        ${pairTypeDesc.shortName}[] entities = new ${pairTypeDesc.shortClassName}[dtos.length];
        for (int i = 0; i < dtos.length; i++) {
            entities[i] = copyTo(new ${pairTypeDesc.shortName}(), dtos[i]);
        }
        return entities;
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
    public List<${pairTypeDesc.shortName}> copyToEntityList(List<${targetClassDesc.shortName}> dtoList) {
        List<${pairTypeDesc.shortName}> entityList = new ArrayList<${pairTypeDesc.shortName}>();
        for (${targetClassDesc.shortName} dto : dtoList) {
            entityList.add(copyTo(new ${pairTypeDesc.shortName}(), dto));
        }
        return entityList;
    }
<#list pairTypeDesc.componentClassDesc.propertyDescs as propertyDesc><#if targetClassDesc.getPropertyDesc(propertyDesc.getName())??><#assign pd = targetClassDesc.getPropertyDesc(propertyDesc.getName())><#if propertyDesc.isWritable() && pd.isReadable()>

    /**
     * Copies '${propertyDesc.name}' property of ${targetClassDesc.shortName} entity to ${pairTypeDesc.shortName} instance's '${propertyDesc.name}' property.
     * 
     * @param entity Destination object.
     * @param dto Source object.
     */
    protected void copy${propertyDesc.name?cap_first}To(${pairTypeDesc.shortName} entity, ${targetClassDesc.shortName} dto) {
        entity.set${propertyDesc.name?cap_first}(convertForEntity(dto.${pd.getterName}(), ${propertyDesc.typeDesc.name}.class));
    }
</#if></#if></#list>

</#list>
}
