package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * システム外部から与えられるパラメータをインジェクトしないことを表すためのアノテーションです。
 * <p>このアノテーションが付与されたSetterに対しては、
 * リクエストパラメータ等のシステム外部から与えられるパラメータがインジェクトされません。
 * </p>
 * <p>このアノテーションはアプリケーションのapp.propertiesの
 * {@link Globals#APPKEY_CORE_REQUESTPARAMETER_STRICTINJECTION}エントリが存在しないか
 * 値がfalseである場合だけ有効です。
 * 上記エントリの値がtrueである場合は、
 * システム外部から与えられるパラメータは{@link RequestParameter}アノテーションがついているメソッドにだけインジェクトされます。
 * 新規にアプリケーションを開発する場合は、上記エントリの値をtrueにしておいてこのアノテーションの代わりに
 * {@link RequestParameter}アノテーションを使うことを推奨します。
 * </p>
 * 
 * @see RequestParameter
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Protected {
}
