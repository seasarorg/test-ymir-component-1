package org.seasar.ymir.constraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.seasar.ymir.mock.MockRequest;

/**
 * 制約をテストするためのTestCaseのベースとなるクラスです。
 * <p>制約をテストしたい場合は、
 * 制約を記述するためのAnnotationクラスと制約チェックを行なうためのConstraintクラスをパラメータに与えて
 * このクラスのサブクラスを作成して下さい。
 * サブクラスでは{@link #getAnnotationClass()}メソッドと
 * {@link #newConstraintComponent()}メソッドを実装して下さい。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
abstract public class ConstraintTestCase<A extends Annotation, C extends Constraint<A>>
        extends TestCase {
    private MockRequest request_ = new MockRequest();

    private String value_;

    /**
     * 制約を記述するためのAnnotationクラスを返します。
     * 
     * @return 制約を記述するためのAnnotationクラス。
     */
    abstract protected Class<A> getAnnotationClass();

    /**
     * 制約チェックを行なうためのConstraintクラスのオブジェクトを生成して返します。
     * 
     * @return 制約チェックを行なうためのConstraintクラスのオブジェクト。
     */
    abstract protected C newConstraintComponent();

    /**
     * このテストケースが持つメソッドのうち、
     * 指定された名前を持ち、String型の引数を1つ持つメソッドのMethodオブジェクトを返します。
     * <p>このメソッドは、テスト対象の制約を表すアノテーションが付与されているメソッドの
     * Methodオブジェクトを取得するためのユーティリティメソッドです。
     * </p>
     * 
     * @param methodName メソッド名。
     * @return Methodオブジェクト。
     * @throws NoSuchMethodException 指定されたメソッド名を持つメソッドが存在しない場合
     */
    protected Method getMethod(String methodName) throws NoSuchMethodException {
        return getClass().getMethod(methodName, new Class[] { String.class });
    }

    /**
     * このテストケースが持つメソッドのうち、
     * 指定されたプロパティ名に対応するsetterメソッドオブジェクトを返します。
     * <p>このメソッドは、テスト対象の制約を表すアノテーションが付与されているメソッドの
     * Methodオブジェクトを取得するためのユーティリティメソッドです。
     * </p>
     * 
     * @param propertyName プロパティ名。
     * @return setterメソッドオブジェクト。
     * @throws NoSuchMethodException 指定されたプロパティのためのsetterメソッドが存在しない場合
     */
    protected Method getSetterMethod(String propertyName)
            throws NoSuchMethodException {
        return getClass().getMethod(toMethodName("set", propertyName),
                new Class[] { String.class });
    }

    String toMethodName(String prefix, String propertyName) {
        if (propertyName.length() > 1
                && Character.isUpperCase(propertyName.charAt(1))) {
            return prefix + propertyName;
        } else {
            return prefix + Character.toUpperCase(propertyName.charAt(0))
                    + propertyName.substring(1);
        }
    }

    /**
     * 制約チェックを行なうために使用するRequestオブジェクトを返します。
     * <p>テストを行なう前に、このオブジェクトが返すRequestオブジェクトの内容を必要に応じて変更しておいて下さい。
     * </p>
     * 
     * @return Requestオブジェクト。
     */
    protected MockRequest getRequest() {
        return request_;
    }

    /**
     * 制約チェックに使用されるRequestオブジェクトを設定します。
     * <p>制約チェックに独自のRequestオブジェクトを使いたい場合はこのメソッドを使って設定して下さい。
     * </p>
     * 
     * @param request Requestオブジェクト。nullを指定してはいけません。
     */
    protected void setRequest(MockRequest request) {
        request_ = request;
    }

    /**
     * 指定されたメソッドやクラスに付与されているアノテーションに従って制約チェックを実行します。
     * 
     * @param element アノテーションが付与されているメソッドやクラスオブジェクト。
     * @throws ConstraintViolatedException 制約チェックに失敗した場合。
     */
    protected void confirm(AnnotatedElement element)
            throws ConstraintViolatedException {
        A annotation = element.getAnnotation(getAnnotationClass());
        newConstraintComponent().confirm(this, request_, annotation, element);
    }
}
