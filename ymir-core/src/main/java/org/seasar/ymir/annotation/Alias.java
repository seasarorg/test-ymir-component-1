package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * アノテーションに別名をつけるためのメタアノテーションです。
 * <p>アノテーションに別名をつけるためのアノテーションをエイリアスアノテーションと呼びます。
 * エイリアスアノテーションを定義する場合、このアノテーションを付与するようにして下さい。
 * またエイリアスアノテーションの元となるアノテーションの型をこのアノテーションの要素として設定して下さい。
 * </p>
 * <p>以下、エイリアスアノテーションを定義する方法について説明します。
 * </p>
 * <p>例えばリクエストヘッダからユーザエージェントを取り出すためのエイリアスアノテーションを作成することを考えます。
 * </p>
 * <p>リクエストヘッダからユーザエージェントを取り出すためには、通常
 * <blockquote>
 *   <code>@In(scopeClass=RequestHeaderScope.class, name="User-Agent")</code>
 * </blockquote>
 * としますが、これを
 * <blockquote>
 *   <code>@UserAgent</code>
 * </blockquote>
 * と書けるようにすることにします。
 * また、インジェクション対象のアクションを限定できるよう、@InアノテーションのactionName要素を
 * <blockquote>
 *   <code>@UserAgent(actionName="XXXX")</code>
 * </blockquote>
 * のように指定できるようにすることにします。
 * </p>
 * <p>この場合、UserAgentアノテーションを次のように定義します。
 * <ul>
 *   <li>UserAgentアノテーションに「<code>@Alias</code>」のようにAliasアノテーションを付与します。</li>
 *   <li>エイリアスの内容を定義するための"z_alias"要素を定義します。
 *       具体的には「<code>@In z_alias() default @In(scopeClass=RequestHeaderScope.class, name="User-Agent")</code>」
 *       のようにz_alias要素のデフォルト値として記述します。
 *       z_alias要素の型はエイリアスするアノテーションの型に合わせます。</li>
 *   <li>値を変更できるよう、actionName要素を定義します。
 *       デフォルト値としては@InでのactionName要素のデフォルト値と同じ値を指定すると良いでしょう。</li>
 * </ul>
 * </p>
 * 
 * @since 0.9.6
 * @author YOKOTA Takehiko
 * @see ElementAlias
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Alias {
}
