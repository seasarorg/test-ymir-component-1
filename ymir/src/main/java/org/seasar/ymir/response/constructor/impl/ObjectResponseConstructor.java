package org.seasar.ymir.response.constructor.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.seasar.ymir.Response;
import org.seasar.ymir.response.VoidResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;
import org.seasar.ymir.response.constructor.ResponseConstructorSelector;

public class ObjectResponseConstructor implements ResponseConstructor<Object> {

    private ResponseConstructorSelector responseConstructorSelector_;

    public Class<Object> getTargetClass() {

        return Object.class;
    }

    @SuppressWarnings("unchecked")
    public Response constructResponse(Object component, Object returnValue) {

        if (returnValue == null) {
            return VoidResponse.INSTANCE;
        }

        ResponseConstructor<?> responseConstructor = findResponseConstructor(returnValue
                .getClass());
        if (responseConstructor == null) {
            if (responseConstructorSelector_
                    .hasResponseConstructor(String.class)) {
                responseConstructor = responseConstructorSelector_
                        .getResponseConstructor(String.class);
                returnValue = returnValue.toString();
            } else {
                throw new RuntimeException(
                        "Can't construct response for return value: class="
                                + returnValue.getClass() + ", value="
                                + returnValue);
            }
        }

        return ((ResponseConstructor<Object>) responseConstructor)
                .constructResponse(component, returnValue);
    }

    ResponseConstructor<?> findResponseConstructor(Class<?> clazz) {
        if (clazz == Object.class) {
            // 無限ループを避けるため。
            return null;
        }

        Set<Class> interfaceSet = new LinkedHashSet<Class>();
        if (clazz.isInterface()) {
            if (responseConstructorSelector_.hasResponseConstructor(clazz)) {
                return responseConstructorSelector_
                        .getResponseConstructor(clazz);
            }
            interfaceSet.addAll(Arrays.asList(clazz.getInterfaces()));
        } else {
            Class<?> type = clazz;
            do {
                if (responseConstructorSelector_.hasResponseConstructor(type)) {
                    return responseConstructorSelector_
                            .getResponseConstructor(type);
                }
                interfaceSet.addAll(Arrays.asList(type.getInterfaces()));
            } while ((type = type.getSuperclass()) != Object.class);
        }

        for (Iterator<Class> itr = interfaceSet.iterator(); itr.hasNext();) {
            Class interfaze = itr.next();
            ResponseConstructor<?> constructor = findResponseConstructor(interfaze);
            if (constructor != null) {
                return constructor;
            }
        }
        return null;
    }

    public void setResponseConstructorSelector(
            ResponseConstructorSelector responseConstructorSelector) {
        responseConstructorSelector_ = responseConstructorSelector;
    }
}
