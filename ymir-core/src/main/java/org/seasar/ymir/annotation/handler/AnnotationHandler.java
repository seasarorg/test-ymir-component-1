package org.seasar.ymir.annotation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.annotation.Alias;
import org.seasar.ymir.annotation.Collection;

/**
 * アノテーションを扱うためのインタフェースです。
 * <p>アノテーションに別名をつけるエイリアスアノテーションや
 * 複数のアノテーションをまとめて付与するためのコレクションアノテーションを適切に解釈するためには、
 * このインタフェース経由でアノテーションを取得する必要があります。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @see Alias
 * @see Collection
 * @since 0.9.6
 * @author YOKOTA Takehiko
 */
public interface AnnotationHandler {
    /**
     * 指定されたアノテーションが付与されているかを返します。
     * <p>エイリアスアノテーションやコレクションアノテーションは展開されます。
     * そのため指定されたアノテーションが付与されていない場合でも、
     * 付与されているエイリアスアノテーションやコレクションアノテーションを展開したものに
     * 指定されたアノテーションが含まれる場合はtrueを返します。
     * </p>
     * <p>要素がnullである場合やアノテーションが付与されていない場合はfalseを返します。</p>
     * 
     * @param element 要素。nullを指定することもできます。
     * @param annotationClass アノテーション型。nullを指定してはいけません。
     * @return 指定されたアノテーションが付与されているか。
     */
    boolean isAnnotationPresent(AnnotatedElement element,
            Class<? extends Annotation> annotationClass);

    /**
     * 指定された要素に付与されているアノテーションのうち、指定された型と合致するアノテーションを返します。
     * <p>コレクションアノテーションやエイリアスアノテーションはともに展開されて返されます。
     * このため{@link AnnotatedElement#getAnnotation(Class)}とは異なり、
     * 同一型のアノテーションが複数返されることがあります。
     * なお返される順序は不定です。
     * </p>
     * <p>要素がnullである場合や該当するアノテーションが存在しない場合は空の配列を返します。</p>
     * 
     * @param element 要素。nullを指定することもできます。
     * @param annotationClass アノテーション型。nullを指定してはいけません。
     * @return 指定された型と合致するアノテーションの配列。存在しない場合は空の配列を返します。
     */
    <T extends Annotation> T[] getAnnotations(AnnotatedElement element,
            Class<T> annotationClass);

    /**
     * 指定された要素に付与されているアノテーションのうち、指定された型と合致するアノテーションを返します。
     * <p>{@link #getAnnotations(AnnotatedElement, Class)}と同じですが、
     * 返されるアノテーションはたかだか1つだけです。
     * </p>
     * <p>アノテーションが2つ以上存在する場合は{@link IllegalStateException}をスローします。
     * </p>
     * 
     * @param element 要素。nullを指定することもできます。
     * @param annotationClass アノテーション型。nullを指定してはいけません。
     * @return 指定された型と合致するアノテーション。存在しない場合はnullを返します。
     * @throws IllegalStateException 合致するアノテーションが複数存在する場合。
     */
    <T extends Annotation> T getAnnotation(AnnotatedElement element,
            Class<T> annotationClass);

    /**
     * 指定された要素に付与されているアノテーションのうち、指定されたメタアノテーションが付与されているアノテーションを返します。
     * <p>コレクションアノテーションやエイリアスアノテーションはともに展開されて返されます。
     * </p>
     * <p>要素がnullである場合や該当するアノテーションが存在しない場合は空の配列を返します。</p>
     * 
     * @param element 要素。nullを指定することもできます。
     * @param annotationClass メタアノテーション型。nullを指定してはいけません。
     * @return 指定されたメタアノテーションが付与されているアノテーションの配列。存在しない場合は空の配列を返します。
     */
    Annotation[] getMarkedAnnotations(AnnotatedElement element,
            Class<? extends Annotation> metaAnnotationClass);
}
