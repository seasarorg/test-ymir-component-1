package org.seasar.ymir.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * アノテーションに別名をつけるためのメタアノテーションです。
 * <p>アノテーションに別名をつけるためのアノテーションをエイリアスアノテーションと呼びます。
 * エイリアスアノテーションを定義する場合、このアノテーションを付与するようにして下さい。
 * またエイリアスアノテーションの元となるアノテーションの型をこのアノテーションのプロパティとして設定して下さい。
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
 * また、インジェクション対象のアクションを限定できるよう、@InアノテーションのactionNameプロパティを
 * <blockquote>
 *   <code>@UserAgent(actionName="XXXX")</code>
 * </blockquote>
 * のように指定できるようにすることにします。
 * </p>
 * <p>この場合、UserAgentアノテーションを次のように定義します。
 * <ul>
 *   <li>UserAgentアノテーションに「<code>@Alias</code>」のようにAliasアノテーションを付与します。</li>
 *   <li>エイリアスの内容を定義するための"_alias"プロパティを定義します。
 *       具体的には「<code>@In _alias() default @In(scopeClass=RequestHeaderScope.class, name="User-Agent")</code>」
 *       のように_aliasプロパティのデフォルト値として記述します。
 *       _aliasプロパティの型はエイリアスするアノテーションの型に合わせます。</li>
 *   <li>値を変更できるよう、actionNameプロパティを定義します。
 *       デフォルト値としては@InでのactionNameプロパティのデフォルト値と同じ値を指定すると良いでしょう。</li>
 * </ul>
 * </p>
 * 
 * @see Final
 * @since 0.9.6
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Alias {
}
