package org.seasar.ymir.zpt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Pageクラスのプロパティのうち、ページに関するパラメータを保持しているものを指定するためのアノテーションです。
 * <p>通常、<code>param-self</code>を使ってページに関するパラメータを取得する場合は
 * Pageクラスが持つGetterメソッドが利用されるため、
 * Pageクラスが持つBeanのGetterメソッドを呼び出すようにしたい場合は
 * Pageクラスに委譲のためのメソッドを作成する必要があります。
 * しかしながらパラメータが大量に存在する場合は非効率的です。
 * このアノテーションで委譲先のプロパティの名称を指定することで、フレームワークが自動的に
 * 委譲処理を行なうようになります。
 * 具体的には、例えば<code>@ParameterHolder("bean")</code>というアノテーションを
 * Pageクラスに付与しておくと、
 * <code>param-self/value</code>の値を取得する場合に<code>Page#getValue()</code>
 * の代わりに<code>Page#getBean()#getValue()</code>が呼び出されるようになります。
 * </p>
 * 
 * @author skirnir
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
public @interface ParameterHolder {
    /**
     * パラメータの取得処理を委譲するプロパティの名称です。
     * 
     * @return パラメータの取得処理を委譲するプロパティの名称。
     */
    String value();
}
