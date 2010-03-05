package org.seasar.ymir.constraint;

import java.lang.reflect.AnnotatedElement;
import java.util.List;

public interface ConstraintBagCreator<T> {
    ConstraintBag<?>[] CONSTRAINTBAG_EMPTY = new ConstraintBag<?>[0];

    Class<T> getTargetClass();

    /**
     * 指定されたクラスに関して{@link ConstraintBag}を作成します。
     * 
     * @param beanClass クラス。nullを指定することもできます。
     * @param decider ConstraintBagを作成する際に指定する{@link ConfirmationDecider}。
     * nullを指定してはいけません。
     * 通常は{@link ConstraintManager#getAlwaysDecider()}で取得できる
     * ConfirmationDeciderを指定して下さい。
     * 作成したConstraintBagをある特定の条件でのみ有効にしたい場合は
     * 独自のConfirmationDeciderオブジェクトを指定して下さい。
     * @return 作成したConstraintBagの配列。
     * このConstraintBagCreatorではConstraintBagを作成できず、
     * かつ別のConstraintBagCreatorにConstraintBagの作成を委譲すべき場合は
     * nullが返されます。
     */
    ConstraintBag<?>[] createConstraintBags(Class<T> beanClass,
            ConfirmationDecider decider);

    /**
     * 指定されたクラスに関して{@link ConstraintBag}を作成します。
     * <p>作成されたConstraintBagは指定されたListに追加されます。
     * </p>
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
     * @return このConstraintBagCreatorではConstraintBagを作成できず、
     * かつ別のConstraintBagCreatorにConstraintBagの作成を委譲すべき場合は
     * falseが返されます。
     * それ以外の場合はtrueが返されます。
     */
    boolean createConstraintBags(Class<T> beanClass, AnnotatedElement element,
            ConfirmationDecider decider, List<ConstraintBag<?>> bags);
}
