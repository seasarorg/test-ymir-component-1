package org.seasar.ymir.constraint.impl;

import org.seasar.ymir.Note;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Length;
import org.seasar.ymir.test.constraint.ConstraintTestCase;

public class LengthConstraintTest extends
        ConstraintTestCase<Length, LengthConstraint> {
    @Override
    protected Class<Length> getAnnotationClass() {
        return Length.class;
    }

    @Override
    protected LengthConstraint newConstraintComponent() {
        return new LengthConstraint();
    }

    @Length(max = 10)
    public void setValue(String value) {
    }

    @Length(min = 10)
    public void setValue2(String value) {
    }

    @Length(10)
    public void setValue3(String value) {
    }

    @Length(min = 5, max = 10)
    public void setValue4(String value) {
    }

    @Length(property = "#hoge1", max = 1)
    public void _get_testValidate6() {
    }

    @Length(min = 5, max = 5)
    public void setValue7(String value) {
    }

    public void testValidate_最大長が指定されている場合() throws Exception {
        getRequest().getParameterMap().put("value",
                new String[] { "1234567890" });

        try {
            confirm(getSetterMethod("value"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        getRequest().getParameterMap().put("value",
                new String[] { "12345678901" });
        try {
            confirm(getSetterMethod("value"));
            fail();
        } catch (ValidationFailedException expected) {
        }
    }

    public void testValidate2_最小長が指定されている場合() throws Exception {
        getRequest().getParameterMap().put("value2",
                new String[] { "1234567890" });

        try {
            confirm(getSetterMethod("value2"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        getRequest().getParameterMap().put("value2",
                new String[] { "123456789" });
        try {
            confirm(getSetterMethod("value2"));
            fail();
        } catch (ValidationFailedException expected) {
        }
    }

    public void testValidate3_最大長が指定されている場合() throws Exception {
        getRequest().getParameterMap().put("value3",
                new String[] { "1234567890" });

        try {
            confirm(getSetterMethod("value3"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        getRequest().getParameterMap().put("value3",
                new String[] { "12345678901" });
        try {
            confirm(getSetterMethod("value3"));
            fail();
        } catch (ValidationFailedException expected) {
        }
    }

    public void testValidate5_パラメータが指定されていない場合は何もしないこと() throws Exception {
        try {
            confirm(getSetterMethod("value4"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }

    public void testValidate4_パラメータが空文字列の場合は何もしないこと() throws Exception {
        getRequest().getParameterMap().put("value4", new String[] { "" });

        try {
            confirm(getSetterMethod("value4"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }

    public void testValidate6_パラメータ名が正規表現で与えられていてマッチするパラメータがなかった場合には何もしないこと()
            throws Exception {
        getRequest().getParameterMap().put("hoge", new String[] { "123" });

        try {
            confirm(getActionMethod("_get_testValidate6"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }

    public void testValidate7_最大値と最小値が同じ場合はメッセージが変わること() throws Exception {
        getRequest().getParameterMap().put("value7", new String[] { "1" });

        try {
            confirm(getSetterMethod("value7"));
        } catch (ValidationFailedException ex) {
            Note[] notes = ex.getNotes().getNotes();
            assertEquals(1, notes.length);
            assertEquals(Constraint.PREFIX_MESSAGEKEY + "length.different",
                    notes[0].getValue());
        }
    }
}