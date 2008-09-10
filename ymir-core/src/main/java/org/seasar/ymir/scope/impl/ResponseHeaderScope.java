package org.seasar.ymir.scope.impl;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.TypeConversionManager;

/**
 * レスポンスヘッダに値を設定するための仮想的なスコープを表すクラスです。
 * <p>{@link #setAttribute(String, Object)}に指定された値の型が{@link Date}型の場合、
 * {@link HttpServletResponse#setDateHeader(String, long)}を使ってヘッダが設定されます。
 * 値の型が{@link Number}型の場合、
 * {@link HttpServletResponse#setIntHeader(String, int)}を使ってヘッダが設定されます。
 * それ以外の場合は
 * {@link TypeConversionManager}を使って値を文字列に変換した上で
 * {@link HttpServletResponse#setHeader(String, String)}を使ってヘッダが設定されます。
 * </p>
 * <p>{@link #setAttribute(String, Object)}に指定された値が配列型の場合は
 * <code>HttpServletResponse#addHeader()</code>を使って全ての値がヘッダとして設定されます。
 * そうでない場合は
 * <code>HttpServletResponse#setHeader()</code>を使ってヘッダが設定されます。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class ResponseHeaderScope extends AbstractServletScope {
    private TypeConversionManager typeConversionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
    }

    public Object getAttribute(String name, Class<?> type) {
        return null;
    }

    public void setAttribute(String name, Object value) {
        if (name == null) {
            return;
        }

        HttpServletResponse response = getResponse();
        if (value == null) {
            ;
        } else if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                addHeader(response, name, Array.get(value, i));
            }
        } else {
            setHeader(response, name, value);
        }
    }

    protected void addHeader(HttpServletResponse response, String name,
            Object value) {
        Class<?> clazz = value.getClass();
        if (Date.class.isAssignableFrom(clazz)) {
            response.addDateHeader(name, ((Date) value).getTime());
        } else if (Number.class.isAssignableFrom(clazz)) {
            response.addIntHeader(name, ((Number) value).intValue());
        } else {
            response.addHeader(name, typeConversionManager_.convert(value,
                    String.class));
        }
    }

    protected void setHeader(HttpServletResponse response, String name,
            Object value) {
        Class<?> clazz = value.getClass();
        if (Date.class.isAssignableFrom(clazz)) {
            response.setDateHeader(name, ((Date) value).getTime());
        } else if (Number.class.isAssignableFrom(clazz)) {
            response.setIntHeader(name, ((Number) value).intValue());
        } else {
            response.setHeader(name, typeConversionManager_.convert(value,
                    String.class));
        }
    }

    public Iterator<String> getAttributeNames() {
        List<String> emptyList = Collections.emptyList();
        return emptyList.iterator();
    }
}
