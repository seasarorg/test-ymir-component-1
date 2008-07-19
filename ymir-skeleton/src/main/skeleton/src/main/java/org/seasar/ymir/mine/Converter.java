package org.seasar.ymir.mine;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Messages;
import org.seasar.ymir.TypeConversionManager;

public class Converter {
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

    final protected <T> T convert(String str, Class<T> type) {
        return typeConversionManager_.convert(str, type);
    }

    final protected String convert(Object obj) {
        return typeConversionManager_.convert(obj);
    }

    final protected boolean isEmpty(Object obj) {
        return (obj == null || obj instanceof String
            && ((String) obj).trim().length() == 0);
    }
}
