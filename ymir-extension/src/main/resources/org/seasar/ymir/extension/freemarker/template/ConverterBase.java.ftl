${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>
<#if !importDesc.empty>

${importDesc.asString}</#if>
/**
 * A class to convert Dto objects and entity objects.
 * <p>Developer can override methods to customize this class's behavior,
 * and add methods to gain conversion ability.
 * </p>
 */
<#list classDesc.annotationDescs as annotationDesc>
${annotationDesc.asShortString}
</#list>
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
<#list targetClassDesc.propertyDescs as propertyDesc>
  <#if propertyDesc.typeDesc.name == "org.seasar.ymir.render.Selector"
       && propertyDesc.name?ends_with("Selector")>
    <#assign name = propertyDesc.name?substring(0, propertyDesc.name?length - 8)>
    <#assign typeShortName = "String">
    <#assign writable = propertyDesc.readable>
  <#elseif propertyDesc.typeDesc.name == "org.seasar.ymir.render.html.Select"
       && propertyDesc.name?ends_with("Select")>
    <#assign name = propertyDesc.name?substring(0, propertyDesc.name?length - 6)>
    <#assign typeShortName = "String">
    <#assign writable = propertyDesc.readable>
  <#else>
    <#assign name = propertyDesc.name>
    <#assign typeShortName = propertyDesc.typeDesc.shortName>
    <#assign writable = propertyDesc.writable>
  </#if>
  <#if pairTypeDesc.componentClassDesc.getPropertyDesc(name)??>
    <#assign pd = pairTypeDesc.componentClassDesc.getPropertyDesc(name)>
    <#if writable && pd.readable>
     *   <li>${name}</li>
    </#if>
  </#if>
</#list>
     * </ul>
     * 
     * @param entity Source object.
     * @param dto Destination object.
     * @return Dto given as the second argument of this method.
     */
    public ${targetClassDesc.shortName} copyEntityToDto(${pairTypeDesc.shortName} entity, ${targetClassDesc.shortName} dto) {
<#list targetClassDesc.propertyDescs as propertyDesc>
  <#if propertyDesc.typeDesc.name == "org.seasar.ymir.render.Selector"
       && propertyDesc.name?ends_with("Selector")>
    <#assign name = propertyDesc.name?substring(0, propertyDesc.name?length - 8)>
    <#assign typeShortName = "String">
    <#assign writable = propertyDesc.readable>
  <#elseif propertyDesc.typeDesc.name == "org.seasar.ymir.render.html.Select"
       && propertyDesc.name?ends_with("Select")>
    <#assign name = propertyDesc.name?substring(0, propertyDesc.name?length - 6)>
    <#assign typeShortName = "String">
    <#assign writable = propertyDesc.readable>
  <#else>
    <#assign name = propertyDesc.name>
    <#assign typeShortName = propertyDesc.typeDesc.shortName>
    <#assign writable = propertyDesc.writable>
  </#if>
  <#if pairTypeDesc.componentClassDesc.getPropertyDesc(name)??>
    <#assign pd = pairTypeDesc.componentClassDesc.getPropertyDesc(name)>
    <#if writable && pd.readable>
        reflect${name?cap_first}ToDto(entity, dto);
    </#if>
  </#if>
</#list>

        return dto;
    }

    /**
     * Copies a List of ${pairTypeDesc.shortName} entity to a List of ${targetClassDesc.shortName}.
     * <p>This methods copies the following properties automatically:</p>
     * <ul>
<#list targetClassDesc.propertyDescs as propertyDesc>
  <#if propertyDesc.typeDesc.name == "org.seasar.ymir.render.Selector"
       && propertyDesc.name?ends_with("Selector")>
    <#assign name = propertyDesc.name?substring(0, propertyDesc.name?length - 8)>
    <#assign typeShortName = "String">
    <#assign writable = propertyDesc.readable>
  <#elseif propertyDesc.typeDesc.name == "org.seasar.ymir.render.html.Select"
       && propertyDesc.name?ends_with("Select")>
    <#assign name = propertyDesc.name?substring(0, propertyDesc.name?length - 6)>
    <#assign typeShortName = "String">
    <#assign writable = propertyDesc.readable>
  <#else>
    <#assign name = propertyDesc.name>
    <#assign typeShortName = propertyDesc.typeDesc.shortName>
    <#assign writable = propertyDesc.writable>
  </#if>
  <#if pairTypeDesc.componentClassDesc.getPropertyDesc(name)??>
    <#assign pd = pairTypeDesc.componentClassDesc.getPropertyDesc(name)>
    <#if writable && pd.readable>
     *   <li>${name}</li>
    </#if>
  </#if>
</#list>
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
<#list targetClassDesc.propertyDescs as propertyDesc>
  <#if propertyDesc.typeDesc.name == "org.seasar.ymir.render.Selector"
       && propertyDesc.name?ends_with("Selector")>
    <#assign name = propertyDesc.name?substring(0, propertyDesc.name?length - 8)>
    <#assign typeShortName = "String">
    <#assign writable = propertyDesc.readable>
  <#elseif propertyDesc.typeDesc.name == "org.seasar.ymir.render.html.Select"
       && propertyDesc.name?ends_with("Select")>
    <#assign name = propertyDesc.name?substring(0, propertyDesc.name?length - 6)>
    <#assign typeShortName = "String">
    <#assign writable = propertyDesc.readable>
  <#else>
    <#assign name = propertyDesc.name>
    <#assign typeShortName = propertyDesc.typeDesc.shortName>
    <#assign writable = propertyDesc.writable>
  </#if>
  <#if pairTypeDesc.componentClassDesc.getPropertyDesc(name)??>
    <#assign pd = pairTypeDesc.componentClassDesc.getPropertyDesc(name)>
    <#if writable && pd.readable>

    /**
     * Copies '${name}' property of ${pairTypeDesc.shortName} entity to ${targetClassDesc.shortName} instance's '${propertyDesc.name}' property.
     * 
     * @param entity Source object.
     * @param dto Destination object.
     */
    protected void reflect${name?cap_first}ToDto(${pairTypeDesc.shortName} entity, ${targetClassDesc.shortName} dto) {
      <#if propertyDesc.name == name + "Selector">
        dto.get${propertyDesc.name?cap_first}().setSelectedValue(extract${name?cap_first}FromEntity(entity));
      <#elseif propertyDesc.name == name + "Select">
        dto.get${propertyDesc.name?cap_first}().setSelectedValue(extract${name?cap_first}FromEntity(entity));
      <#else>
        dto.set${name?cap_first}(extract${name?cap_first}FromEntity(entity));
      </#if>
    }

    /**
     * Extracts '${name}' property of ${pairTypeDesc.shortName} entity in order to copy to ${targetClassDesc.shortName} instance's '${propertyDesc.name}' property.
     * 
     * @param entity Source object.
     * @return Extracted value.
     */
    protected ${typeShortName} extract${name?cap_first}FromEntity(${pairTypeDesc.shortName} entity) {
        return convert(entity.${pd.getterName}(), ${typeShortName}.class);
    }
    </#if>
  </#if>
</#list>

    /**
     * Copies ${targetClassDesc.shortName} entity to ${pairTypeDesc.shortName} instance.
     * <p>This methods copies the following properties automatically:</p>
     * <ul>
<#list pairTypeDesc.componentClassDesc.propertyDescs as propertyDesc>
  <#if targetClassDesc.getPropertyDesc(propertyDesc.name)??>
    <#assign name = propertyDesc.name>
  <#elseif targetClassDesc.getPropertyDesc(propertyDesc.name + "Selector")??
           && targetClassDesc.getPropertyDesc(propertyDesc.name + "Selector").typeDesc.name == "org.seasar.ymir.render.Selector">
    <#assign name = propertyDesc.name + "Selector">
  <#elseif targetClassDesc.getPropertyDesc(propertyDesc.name + "Select")??
           && targetClassDesc.getPropertyDesc(propertyDesc.name + "Select").typeDesc.name == "org.seasar.ymir.render.html.Select">
    <#assign name = propertyDesc.name + "Select">
  <#else>
    <#assign name = "">
  </#if>
  <#if name != "">
    <#assign pd = targetClassDesc.getPropertyDesc(name)>
    <#if propertyDesc.writable && pd.readable>
     *   <li>${propertyDesc.name}</li>
    </#if>
  </#if>
</#list>
     * </ul>
     * 
     * @param dto Source object.
     * @param entity Destination object.
     * @return Entity given as the second argument of this method.
     */
    public ${pairTypeDesc.shortName} copyDtoToEntity(${targetClassDesc.shortName} dto, ${pairTypeDesc.shortName} entity) {
<#list pairTypeDesc.componentClassDesc.propertyDescs as propertyDesc>
  <#if targetClassDesc.getPropertyDesc(propertyDesc.name)??>
    <#assign name = propertyDesc.name>
  <#elseif targetClassDesc.getPropertyDesc(propertyDesc.name + "Selector")??
           && targetClassDesc.getPropertyDesc(propertyDesc.name + "Selector").typeDesc.name == "org.seasar.ymir.render.Selector">
    <#assign name = propertyDesc.name + "Selector">
  <#elseif targetClassDesc.getPropertyDesc(propertyDesc.name + "Select")??
           && targetClassDesc.getPropertyDesc(propertyDesc.name + "Select").typeDesc.name == "org.seasar.ymir.render.html.Select">
    <#assign name = propertyDesc.name + "Select">
  <#else>
    <#assign name = "">
  </#if>
  <#if name != "">
    <#assign pd = targetClassDesc.getPropertyDesc(name)>
    <#if propertyDesc.writable && pd.readable>
        reflect${propertyDesc.name?cap_first}ToEntity(dto, entity);
    </#if>
  </#if>
</#list>

        return entity;
    }

    /**
     * Copies a List of ${targetClassDesc.shortName} entity to a List of ${pairTypeDesc.shortName}.
     * <p>This methods copies the following properties automatically:</p>
     * <ul>
<#list pairTypeDesc.componentClassDesc.propertyDescs as propertyDesc>
  <#if targetClassDesc.getPropertyDesc(propertyDesc.name)??>
    <#assign name = propertyDesc.name>
  <#elseif targetClassDesc.getPropertyDesc(propertyDesc.name + "Selector")??
           && targetClassDesc.getPropertyDesc(propertyDesc.name + "Selector").typeDesc.name == "org.seasar.ymir.render.Selector">
    <#assign name = propertyDesc.name + "Selector">
  <#elseif targetClassDesc.getPropertyDesc(propertyDesc.name + "Select")??
           && targetClassDesc.getPropertyDesc(propertyDesc.name + "Select").typeDesc.name == "org.seasar.ymir.render.html.Select">
    <#assign name = propertyDesc.name + "Select">
  <#else>
    <#assign name = "">
  </#if>
  <#if name != "">
    <#assign pd = targetClassDesc.getPropertyDesc(name)>
    <#if propertyDesc.writable && pd.readable>
     *   <li>${propertyDesc.name}</li>
    </#if>
  </#if>
</#list>
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
<#list pairTypeDesc.componentClassDesc.propertyDescs as propertyDesc>
  <#if targetClassDesc.getPropertyDesc(propertyDesc.name)??>
    <#assign name = propertyDesc.name>
  <#elseif targetClassDesc.getPropertyDesc(propertyDesc.name + "Selector")??
           && targetClassDesc.getPropertyDesc(propertyDesc.name + "Selector").typeDesc.name == "org.seasar.ymir.render.Selector">
    <#assign name = propertyDesc.name + "Selector">
  <#elseif targetClassDesc.getPropertyDesc(propertyDesc.name + "Select")??
           && targetClassDesc.getPropertyDesc(propertyDesc.name + "Select").typeDesc.name == "org.seasar.ymir.render.html.Select">
    <#assign name = propertyDesc.name + "Select">
  <#else>
    <#assign name = "">
  </#if>
  <#if name != "">
    <#assign pd = targetClassDesc.getPropertyDesc(name)>
    <#if propertyDesc.writable && pd.readable>

    /**
     * Copies '${name}' property of ${targetClassDesc.shortName} entity to ${pairTypeDesc.shortName} instance's '${propertyDesc.name}' property.
     * 
     * @param dto Source object.
     * @param entity Destination object.
     */
    protected void reflect${propertyDesc.name?cap_first}ToEntity(${targetClassDesc.shortName} dto, ${pairTypeDesc.shortName} entity) {
        entity.set${propertyDesc.name?cap_first}(extract${propertyDesc.name?cap_first}FromDto(dto));
    }

    /**
     * Extracts '${name}' property of ${targetClassDesc.shortName} entity in order to copy to ${pairTypeDesc.shortName} instance's '${propertyDesc.name}' property.
     * 
     * @param dto Source object.
     * @return Extracted value.
     */
    protected ${propertyDesc.typeDesc.shortName} extract${propertyDesc.name?cap_first}FromDto(${targetClassDesc.shortName} dto) {
      <#if name == propertyDesc.name + "Selector">
        return convertForEntity(dto.${pd.getterName}().getSelectedValue(), ${propertyDesc.typeDesc.shortName}.class);
      <#elseif name == propertyDesc.name + "Select">
        return convertForEntity(dto.${pd.getterName}().getSelectedValue(), ${propertyDesc.typeDesc.shortName}.class);
      <#else>
        return convertForEntity(dto.${pd.getterName}(), ${propertyDesc.typeDesc.shortName}.class);
      </#if>
    }
    </#if>
  </#if>
</#list>

</#list>
}
