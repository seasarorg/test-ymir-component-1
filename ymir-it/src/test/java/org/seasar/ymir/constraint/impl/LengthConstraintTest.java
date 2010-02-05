package org.seasar.ymir.constraint.impl;

import org.seasar.ymir.constraint.Globals;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Length;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.testing.constraint.ConstraintTestCase;

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

    @Length(max = 10, messageKey = "KEY")
    public void setValue8(String value) {
    }

    @Length(max = 10, messageKey = "!KEY")
    public void setValue9(String value) {
    }

    @Length(min = 10, messageKey = "KEY")
    public void setValue10(String value) {
    }

    @Length(min = 10, messageKey = "!KEY")
    public void setValue11(String value) {
    }

    @Length(min = 5, max = 5, messageKey = "KEY")
    public void setValue12(String value) {
    }

    @Length(min = 5, max = 5, messageKey = "!KEY")
    public void setValue13(String value) {
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
            assertEquals(Globals.PREFIX_MESSAGEKEY + "length.max", expected
                    .getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value8",
                new String[] { "12345678901" });
        try {
            confirm(getSetterMethod("value8"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "length.max.KEY", expected
                    .getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value9",
                new String[] { "12345678901" });
        try {
            confirm(getSetterMethod("value9"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals("KEY", expected.getNotes().getNotes()[0].getValue());
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
            assertEquals(Globals.PREFIX_MESSAGEKEY + "length.min", expected
                    .getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value10",
                new String[] { "123456789" });
        try {
            confirm(getSetterMethod("value10"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "length.min.KEY", expected
                    .getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value11",
                new String[] { "123456789" });
        try {
            confirm(getSetterMethod("value11"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals("KEY", expected.getNotes().getNotes()[0].getValue());
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
            assertEquals(Globals.PREFIX_MESSAGEKEY + "length.different",
                    notes[0].getValue());
        }

        getRequest().getParameterMap().put("value12", new String[] { "1" });
        try {
            confirm(getSetterMethod("value12"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "length.different.KEY",
                    expected.getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value13", new String[] { "1" });
        try {
            confirm(getSetterMethod("value13"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals("KEY", expected.getNotes().getNotes()[0].getValue());
        }
    }
}
