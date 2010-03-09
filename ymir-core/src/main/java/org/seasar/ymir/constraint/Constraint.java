package org.seasar.ymir.constraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.annotation.SuppressConstraints;
import org.seasar.ymir.constraint.annotation.Validator;

/**
 * 制約チェックを表すインタフェースです。
 * <p>制約とは、HTTPリクエストを受けて対応するPageオブジェクトのアクションメソッドを実行する際に
 * 満たしているべき条件です。
 * フレームワークはアクションメソッドの実行に先立って制約チェックを行ない、
 * 制約が満たされていない場合は{@link ConstraintViolatedException}例外クラスまたはそのサブクラスのオブジェクトをスローします。
 * </p>
 * <p>システムに制約を追加するには、
 * このインタフェースの実装クラスである制約チェック処理を表すクラスと、
 * Pageクラスに制約を指定するためのアノテーションを用意する必要があります。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 *
 * @param <T> Pageクラスに制約を指定するためのアノテーションの型。
 * @see ConstraintViolatedException
 * @see Validator
 * @see SuppressConstraints
 * @see ConstraintBundle
 * @author YOKOTA Takehiko
 */
public interface Constraint<T extends Annotation> {
    /**
     * 制約エラーを表すメッセージ文のキーの接頭辞です。
     */
    String PREFIX_MESSAGEKEY = Globals.PREFIX_MESSAGEKEY;

    /**
     * 制約チェックを行ないます。
     * <p>指定されたPageオブジェクトに関して制約チェックを行ないます。
     * 制約を満たしている場合は何もしません。
     * 制約を満たしていない場合は{@link ConstraintViolatedException}例外クラス
     * またはそのサブクラスをスローします。
     * </p>
     * 
     * @param component Pageオブジェクト。
     * @param request 現在のRequestオブジェクト。
     * @param annotation 制約を表すアノテーション。
     * @param element 制約を表すアノテーションが付与されていた要素。
     * アノテーションが要素由来でない場合はnullを渡して下さい。
     * @throws ConstraintViolatedException 制約を満たしていない場合。
     */
    void confirm(Object component, Request request, T annotation,
            AnnotatedElement element) throws ConstraintViolatedException;
}
