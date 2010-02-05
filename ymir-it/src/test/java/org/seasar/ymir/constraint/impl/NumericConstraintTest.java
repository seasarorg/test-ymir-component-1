package org.seasar.ymir.constraint.impl;

import org.seasar.ymir.constraint.Globals;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Numeric;
import org.seasar.ymir.testing.constraint.ConstraintTestCase;

public class NumericConstraintTest extends
        ConstraintTestCase<Numeric, NumericConstraint> {
    @Override
    protected Class<Numeric> getAnnotationClass() {
        return Numeric.class;
    }

    @Override
    protected NumericConstraint newConstraintComponent() {
        return new NumericConstraint();
    }

    @Numeric(greaterEqual = 10)
    public void setValue(String value) {
    }

    @Numeric(greaterThan = 10)
    public void setValue2(String value) {
    }

    @Numeric(lessEqual = 10)
    public void setValue3(String value) {
    }

    @Numeric(lessThan = 10)
    public void setValue4(String value) {
    }

    @Numeric(integer = true)
    public void setValue5(String value) {
    }

    @Numeric(greaterEqual = 10, messageKey = "KEY")
    public void setValue6(String value) {
    }

    @Numeric(greaterEqual = 10, messageKey = "!KEY")
    public void setValue7(String value) {
    }

    @Numeric(greaterThan = 10, messageKey = "KEY")
    public void setValue8(String value) {
    }

    @Numeric(greaterThan = 10, messageKey = "!KEY")
    public void setValue9(String value) {
    }

    @Numeric(lessEqual = 10, messageKey = "KEY")
    public void setValue10(String value) {
    }

    @Numeric(lessEqual = 10, messageKey = "!KEY")
    public void setValue11(String value) {
    }

    @Numeric(lessThan = 10, messageKey = "KEY")
    public void setValue12(String value) {
    }

    @Numeric(lessThan = 10, messageKey = "!KEY")
    public void setValue13(String value) {
    }

    @Numeric(integer = true, messageKey = "KEY")
    public void setValue14(String value) {
    }

    @Numeric(integer = true, messageKey = "!KEY")
    public void setValue15(String value) {
    }

    public void testValidate_greaterEqual() throws Exception {
        getRequest().getParameterMap().put("value", new String[] { "10" });

        try {
            confirm(getSetterMethod("value"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        getRequest().getParameterMap().put("value", new String[] { "9" });
        try {
            confirm(getSetterMethod("value"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "numeric.greaterEqual",
                    expected.getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value6", new String[] { "9" });
        try {
            confirm(getSetterMethod("value6"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(
                    Globals.PREFIX_MESSAGEKEY + "numeric.greaterEqual.KEY",
                    expected.getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value7", new String[] { "9" });
        try {
            confirm(getSetterMethod("value7"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals("KEY", expected.getNotes().getNotes()[0].getValue());
        }
    }

    public void testValidate_greaterThan() throws Exception {
        getRequest().getParameterMap().put("value2", new String[] { "11" });

        try {
            confirm(getSetterMethod("value2"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        getRequest().getParameterMap().put("value2", new String[] { "10" });
        try {
            confirm(getSetterMethod("value2"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "numeric.greaterThan",
                    expected.getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value8", new String[] { "10" });
        try {
            confirm(getSetterMethod("value8"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "numeric.greaterThan.KEY",
                    expected.getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value9", new String[] { "10" });
        try {
            confirm(getSetterMethod("value9"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals("KEY", expected.getNotes().getNotes()[0].getValue());
        }
    }

    public void testValidate_lessEqual() throws Exception {
        getRequest().getParameterMap().put("value3", new String[] { "10" });

        try {
            confirm(getSetterMethod("value3"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        getRequest().getParameterMap().put("value3", new String[] { "11" });
        try {
            confirm(getSetterMethod("value3"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "numeric.lessEqual",
                    expected.getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value10", new String[] { "11" });
        try {
            confirm(getSetterMethod("value10"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "numeric.lessEqual.KEY",
                    expected.getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value11", new String[] { "11" });
        try {
            confirm(getSetterMethod("value11"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals("KEY", expected.getNotes().getNotes()[0].getValue());
        }
    }

    public void testValidate_lessThan() throws Exception {
        getRequest().getParameterMap().put("value4", new String[] { "9" });

        try {
            confirm(getSetterMethod("value4"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        getRequest().getParameterMap().put("value4", new String[] { "10" });
        try {
            confirm(getSetterMethod("value4"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "numeric.lessThan",
                    expected.getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value12", new String[] { "10" });
        try {
            confirm(getSetterMethod("value12"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "numeric.lessThan.KEY",
                    expected.getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value13", new String[] { "10" });
        try {
            confirm(getSetterMethod("value13"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals("KEY", expected.getNotes().getNotes()[0].getValue());
        }
    }

    public void testValidate_integer() throws Exception {
        getRequest().getParameterMap().put("value5", new String[] { "10" });

        try {
            confirm(getSetterMethod("value5"));
        } catch (ValidationFailedException ex) {
            fail();
        }

        getRequest().getParameterMap().put("value5", new String[] { "10.1" });
        try {
            confirm(getSetterMethod("value5"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "numeric.integer",
                    expected.getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value14", new String[] { "10.1" });
        try {
            confirm(getSetterMethod("value14"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals(Globals.PREFIX_MESSAGEKEY + "numeric.integer.KEY",
                    expected.getNotes().getNotes()[0].getValue());
        }

        getRequest().getParameterMap().put("value15", new String[] { "10.1" });
        try {
            confirm(getSetterMethod("value15"));
            fail();
        } catch (ValidationFailedException expected) {
            assertEquals("KEY", expected.getNotes().getNotes()[0].getValue());
        }
    }

    public void testValidate5_パラメータが指定されていない場合は何もしないこと() throws Exception {
        try {
            confirm(getSetterMethod("value"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }

    public void testValidate4_パラメータが空文字列の場合は何もしないこと() throws Exception {
        getRequest().getParameterMap().put("value", new String[] { "" });

        try {
            confirm(getSetterMethod("value"));
        } catch (ValidationFailedException ex) {
            fail();
        }
    }
}
