${preamble}<#if classDesc.packageName != "">package ${classDesc.packageName};</#if>

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Messages;
import org.seasar.ymir.TypeConversionManager;

public class ${classDesc.shortName} {
    protected TypeConversionManager typeConversionManager_;

    protected Messages messages_;

    @Binding(bindingType = BindingType.MUST)
    final public void setConversionManager(
        TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    final public void setMessages(Messages messages) {
        messages_ = messages;
    }

    final protected TypeConversionManager getTypeConversionManager() {
        return typeConversionManager_;
    }

    final protected Messages getMessages() {
        return messages_;
    }

    @SuppressWarnings("unchecked")
    final protected <T> T convert(Object value, Class<T> type) {
        return (T) typeConversionManager_.convert((Object) value, type);
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
}
