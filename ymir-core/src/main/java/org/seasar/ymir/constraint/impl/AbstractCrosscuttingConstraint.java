package org.seasar.ymir.constraint.impl;

import static org.seasar.ymir.constraint.Globals.PREFIX_REGEX;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.ConstraintUtils;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.CrosscuttingConstraint;
import org.seasar.ymir.constraint.Globals;
import org.seasar.ymir.constraint.annotation.SuppressConstraints;
import org.seasar.ymir.util.BeanUtils;

/**
 * {@link CrosscuttingConstraint}の実装クラスを作成する際にベースとすることのできる
 * 抽象クラスです。
 * 
 * @author skirnir
 * @since 1.0.7
 */
abstract public class AbstractCrosscuttingConstraint implements
        CrosscuttingConstraint {
    /**
     * このオブジェクトが実現する制約チェックが{@link SuppressConstraints}に従うかどうかを返します。
     * <p>このオブジェクトが実現する制約チェックがSuppressConstraintsの指定によらずチェックしたい制約
     * である場合はfalseを返すようにオーバライドして下さい。
     * </p>
     * 
     * @return SuppressConstraintsに従うかどうか。
     */
    protected boolean obeySuppression() {
        return true;
    }

    final public boolean isConfirmed(Object component, Request request,
            ConstraintType type, Set<ConstraintType> suppressTypeSet) {
        if (obeySuppression() && suppressTypeSet.contains(type)) {
            return false;
        }

        return isConfirmed(component, request);
    }

    /**
     * 制約チェックを実行するかどうかを返します。
     * <p>条件によって制約チェックを実行したくない場合はこのメソッドをオーバライドして下さい。
     * </p>
     * 
     * @param component Pageオブジェクト。
     * @param request 現在のRequestオブジェクト。
     * @return 制約チェックを実行するかどうか。
     */
    protected boolean isConfirmed(Object component, Request request) {
        return true;
    }
}