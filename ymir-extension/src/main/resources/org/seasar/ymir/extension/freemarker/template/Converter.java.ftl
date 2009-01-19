${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

public class ${classDesc.shortName} extends ${classDesc.shortName}Base {
    /*
     * For example, if you want to convert empty String as null value
     * when it is set to an entity, override this method by uncommenting
     * the following lines.
     *
     * @see ${classDesc.shortName}Base#convertForEntity(Object, Class)
     */      
//    protected <T> T convertForEntity(Object value, Class<T> type) {
//        return emptyToNull(${fieldPrefix}typeConversionManager${fieldSuffix}.convert(value, type));
//    }
}
