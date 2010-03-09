package org.seasar.ymir.constraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Set;

import org.seasar.ymir.Request;
import org.seasar.ymir.message.Notes;

public interface ConstraintManager {
    ConstraintBag<?>[] CONSTRAINTBAGS_EMPTY = new ConstraintBag<?>[0];

    ConfirmationDecider getAlwaysDecider();

    ConfirmationDecider getDependsOnSuppressTypeDecider();

    void confirmConstraint(ConstraintBag<?>[] bags,
            Set<ConstraintType> suppressTypeSet, Object bean, Request request,
            Notes notes) throws PermissionDeniedException;

    void confirmConstraint(Class<?> beanClass,
            Set<ConstraintType> suppressTypeSet, Object bean, Request request,
            Notes notes) throws PermissionDeniedException;

    void confirmConstraint(Object component, Request request,
            Annotation annotation, AnnotatedElement element)
            throws ConstraintViolatedException;

    /**
     * 指定されたクラスに関する{@link ConstraintBag}を返します。
     * 
     * @param beanClass クラス。nullを指定することもできます。
     * @param decider ConstraintBagを作成する際に指定する{@link ConfirmationDecider}。
     * nullを指定してはいけません。
     * 通常は{@link ConstraintManager#getAlwaysDecider()}で取得できる
     * ConfirmationDeciderを指定して下さい。
     * 作成したConstraintBagをある特定の条件でのみ有効にしたい場合は
     * 独自のConfirmationDeciderオブジェクトを指定して下さい。
     * @return ConstraintBagの配列。
     */
    ConstraintBag<?>[] getConstraintBagsForClass(Class<?> beanClass,
            ConfirmationDecider decider);

    ConstraintBag<?>[] getConstraintBags(AnnotatedElement element,
            ConfirmationDecider decider);

    /**
     * 指定されたクラスに関する{@link ConstraintBag}を指定されたListに追加します。
     * 
     * @param beanClass クラス。nullを指定することもできます。
     * @param decider ConstraintBagを作成する際に指定する{@link ConfirmationDecider}。
     * nullを指定してはいけません。
     * 通常は{@link ConstraintManager#getAlwaysDecider()}で取得できる
     * ConfirmationDeciderを指定して下さい。
     * 作成したConstraintBagをある特定の条件でのみ有効にしたい場合は
     * 独自のConfirmationDeciderオブジェクトを指定して下さい。
     * @param bags 作成したConstraintBagが追加されるList。
     * nullを指定してはいけません。
     */
    void getConstraintBags(AnnotatedElement element,
            ConfirmationDecider decider, List<ConstraintBag<?>> bags);
}
