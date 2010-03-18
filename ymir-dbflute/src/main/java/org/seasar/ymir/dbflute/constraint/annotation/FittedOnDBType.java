package org.seasar.ymir.dbflute.constraint.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.dbflute.constraint.FittedOnDBTypeConstraint;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@ConstraintAnnotation(type = ConstraintType.VALIDATION, component = FittedOnDBTypeConstraint.class)
public @interface FittedOnDBType {
    /**
     * 型チェックを抑制したいカラムに対応するプロパティ名です。
     * 
     * @return 型チェックを抑制したいカラムに対応するプロパティ名。
     */
    String[] suppressTypeCheckFor() default {};

    /**
     * 空チェックを抑制したいカラムに対応するプロパティ名です。
     * 
     * @return 空チェックを抑制したいカラムに対応するプロパティ名。
     */
    String[] suppressEmptyCheckFor() default {};

    /**
     * サイズチェックを抑制したいカラムに対応するプロパティ名です。
     * 
     * @return サイズチェックを抑制したいカラムに対応するプロパティ名。
     */
    String[] suppressSizeCheckFor() default {};

    /**
     * エラーメッセージのキーです。
     * <p>通常はエラーメッセージのキーは「error.constraint.XXXX」ですが、
     * 例えばこのメンバの値を「abc」とするとキーが「error.constraint.XXXX.abc」になります。
     * </p>
     * <p>キー全体を指定したい場合は先頭に「!」をつけて下さい。
     * 例えばメンバの値を「!error.custom」とするとキーは「error.custom」になります。
     * </p>
     * 
     * @return エラーメッセージのキー。
     */
    String messageKey() default "";

    /**
     * エラーメッセージのカテゴリに付与する接頭語です。
     * <p>あるパラメータに関するエラーメッセージはパラメータ名と同じ名前のカテゴリに属するようにNotesオブジェクトに追加されます。
     * このメンバを指定すると、このメンバの値がカテゴリ名の先頭に付与されるようになります。
     * </p>
     * 
     * @return カテゴリに付与する接頭語。
     */
    String namePrefixOnNote() default "";
}
